package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.request.LoginRequest;
import com.heroku.api.request.Request;
import com.ning.http.client.*;
import com.ning.http.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class AsyncHttpClientConnection implements Connection<ListenableFuture<?>> {

    private String apiKey;
    private AsyncHttpClient httpClient;


    public AsyncHttpClientConnection(LoginRequest login) {
        httpClient = geHttpClient();
        this.apiKey = execute(login).getApi_key();
    }

    public AsyncHttpClientConnection(String apiKey) {
        httpClient = geHttpClient();
        this.apiKey = apiKey;
    }

    protected AsyncHttpClient geHttpClient() {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        // TODO: fix the handling of hostname verification
        if (!Heroku.Config.ENDPOINT.isDefault()) {
            builder.setSSLContext(Heroku.herokuSSLContext());
            builder.setHostnameVerifier(Heroku.herokuHostnameVerifier());
        }
        return new AsyncHttpClient(builder.build());
    }

    private com.ning.http.client.Request buildRequest(Request<?> req) {
        AsyncHttpClient.BoundRequestBuilder builder = prepareRequest(req);
        builder.setHeader(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
        builder.setHeader(req.getResponseType().getHeaderName(), req.getResponseType().getHeaderValue());
        if (apiKey != null) {
            try {
                builder.setHeader("Authorization", "Basic " + Base64.encode((":" + apiKey).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new HerokuAPIException("UnsupportedEncodingException while encoding api key");
            }
        }
        if (req.hasBody()) {
            builder.setBody(req.getBody());
        }
        return builder.build();
    }

    private AsyncHttpClient.BoundRequestBuilder prepareRequest(Request<?> request) {
        String requestEndpoint = Heroku.Config.ENDPOINT.value + request.getEndpoint();
        switch (request.getHttpMethod()) {
            case GET:
                return httpClient.prepareGet(requestEndpoint);
            case POST:
                return httpClient.preparePost(requestEndpoint);
            case PUT:
                return httpClient.preparePut(requestEndpoint);
            case DELETE:
                return httpClient.prepareDelete(requestEndpoint);
            default:
                throw new UnsupportedOperationException(request.getHttpMethod().name() + " is not supported");
        }
    }

    @Override
    public <T> T execute(Request<T> req) {
        try {
            return executeAsync(req).get(30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new HerokuAPIException("request interrupted", e);
        } catch (ExecutionException e) {
            throw new HerokuAPIException("execution exception", e.getCause());
        } catch (TimeoutException e) {
            throw new HerokuAPIException("request timeout after 30 sec", e.getCause());
        }
    }

    @Override
    public <T> ListenableFuture<T> executeAsync(final Request<T> request) {
        com.ning.http.client.Request asyncRequest = buildRequest(request);
        AsyncCompletionHandler<T> handler = new AsyncCompletionHandler<T>() {
            @Override
            public T onCompleted(Response response) throws Exception {
                return request.getResponse(response.getResponseBody("UTF-8").getBytes(), response.getStatusCode());
            }
        };
        try {
            return httpClient.executeRequest(asyncRequest, handler);
        } catch (IOException e) {
            throw new HerokuAPIException("IOException while executing request", e);
        }
    }

    @Override
    public void close() {
        httpClient.close();
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
