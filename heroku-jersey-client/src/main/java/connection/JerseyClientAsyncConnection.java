package connection;

import com.heroku.api.Heroku;
import com.heroku.api.connection.AsyncConnection;
import com.heroku.api.connection.Connection;
import com.heroku.api.connection.ConnectionProvider;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.heroku.api.Heroku.Config.ENDPOINT;

public class JerseyClientAsyncConnection implements AsyncConnection<Future<?>> {

    private final Client client;

    public JerseyClientAsyncConnection() {
        final ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(config);
    }

    @Override
    public <T> Future<T> executeAsync(final Request<T> request, final String apiKey) {
        final AsyncWebResource resource = client.asyncResource(ENDPOINT.value + request.getEndpoint());

        resource.header(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
        resource.header(request.getResponseType().getHeaderName(), request.getResponseType().getHeaderValue());
        resource.header(Http.UserAgent.LATEST.getHeaderName(), Http.UserAgent.LATEST.getHeaderValue("jersey-client"));

        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            resource.header(header.getKey(), header.getValue());
        }

        resource.addFilter(new HTTPBasicAuthFilter("", apiKey));

        final Future<ClientResponse> futureResponse;
        if (request.hasBody()) {
            futureResponse = resource.method(request.getHttpMethod().name(), ClientResponse.class, request.getBody());
        } else {
            futureResponse = resource.method(request.getHttpMethod().name(), ClientResponse.class);
        }

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
                final ClientResponse r = futureResponse.get();
                return request.getResponse(r.getEntity(byte[].class), r.getStatus());
            }

            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                final ClientResponse r = futureResponse.get(timeout, unit);
                return request.getResponse(r.getEntity(byte[].class), r.getStatus());
            }
        };
    }

    @Override
    public <T> T execute(Request<T> request, String key) {
        try {
            return executeAsync(request, key).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
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
