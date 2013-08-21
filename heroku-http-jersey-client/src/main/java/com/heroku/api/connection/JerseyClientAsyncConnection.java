package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.FeaturesAndProperties;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.heroku.api.Heroku.Config.ENDPOINT;

public class JerseyClientAsyncConnection implements AsyncConnection<Future<?>> {

    private final Client client;

    public JerseyClientAsyncConnection() {
        this(Client.create());
    }

    public JerseyClientAsyncConnection(Client client) {
        this.client = client;
    }

    @Override
    public <T> Future<T> executeAsync(final Request<T> request, final Map<String,String> extraHeaders, final String apiKey) {
        final AsyncWebResource resource = client.asyncResource(ENDPOINT.value + request.getEndpoint());
        resource.addFilter(new HTTPBasicAuthFilter("", apiKey));

        final AsyncWebResource.Builder builder = resource.getRequestBuilder();

        builder.header(request.getResponseType().getHeaderName(), request.getResponseType().getHeaderValue());
        builder.header(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
        builder.header(Http.UserAgent.LATEST.getHeaderName(), Http.UserAgent.LATEST.getHeaderValue("jersey-client"));
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }

        if (request.hasBody()) {
            builder.entity(request.getBody());
        }

        final Future<ClientResponse> futureResponse = builder.method(request.getHttpMethod().name(), ClientResponse.class);

        // transform Future<ClientResponse> to Future<T>
        return new Future<T>() {
            public boolean cancel(boolean mayInterruptIfRunning) {
                return futureResponse.cancel(mayInterruptIfRunning);
            }

            public boolean isCancelled() {
                return futureResponse.isCancelled();
            }

            public boolean isDone() {
                return futureResponse.isDone();
            }

            public T get() throws InterruptedException, ExecutionException {
                return handleResponse(futureResponse.get());
            }

            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return handleResponse(futureResponse.get(timeout, unit));
            }

            private T handleResponse(ClientResponse r) {
                return request.getResponse(r.getEntity(byte[].class), r.getStatus());
            }
        };
    }

    @Override
    public <T> Future<T> executeAsync(Request<T> request, String apiKey) {
        return executeAsync(request, Collections.<String, String>emptyMap(), apiKey);
    }

    @Override
    public <T> T execute(Request<T> request, String apiKey) {
        return execute(request, Collections.<String, String>emptyMap(), apiKey);
    }

    @Override
    public <T> T execute(Request<T> request, Map<String,String> extraHeaders,  String key) {
        try {
            return executeAsync(request, extraHeaders, key).get();
        } catch (InterruptedException e) {
            throw new HerokuAPIException(e);
        } catch (ExecutionException e) {
            throw new HerokuAPIException(e);
        }
    }

    @Override
    public void close() {
        client.destroy();
    }

    public static class Provider implements ConnectionProvider {
        @Override
        public Connection getConnection() {
            return new JerseyClientAsyncConnection();
        }
    }
}
