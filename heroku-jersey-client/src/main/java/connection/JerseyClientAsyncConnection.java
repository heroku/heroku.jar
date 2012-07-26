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
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.heroku.api.Heroku.Config.ENDPOINT;

public class JerseyClientAsyncConnection implements AsyncConnection<Future<?>> {

    private final Client client = Client.create();

    @Override
    public <T> Future<T> executeAsync(final Request<T> request, final String apiKey) {
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
