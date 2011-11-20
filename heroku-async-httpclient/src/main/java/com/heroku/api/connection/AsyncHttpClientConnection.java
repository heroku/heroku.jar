package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.exception.HerokuAPIException;
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


    public AsyncHttpClientConnection(LoginCommand login) {
        httpClient = geHttpClient();
        this.apiKey = executeCommand(login).api_key();
    }

    public AsyncHttpClientConnection(String apiKey) {
        httpClient = geHttpClient();
        this.apiKey = apiKey;
    }

    protected AsyncHttpClient geHttpClient() {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setSSLContext(Heroku.herokuSSLContext());
        builder.setHostnameVerifier(Heroku.herokuHostnameVerifier());
        return new AsyncHttpClient(builder.build());
    }

    private Request buildRequest(Command<?> command) {
        AsyncHttpClient.BoundRequestBuilder builder = prepareRequest(command);
        builder.setHeader(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
        builder.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());
        if (apiKey != null) {
            try {
                builder.setHeader("Authorization", "Basic " + Base64.encode((":" + apiKey).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new HerokuAPIException("UnsupportedEncodingException while encoding api key");
            }
        }
        if (command.hasBody()) {
            builder.setBody(command.getBody());
        }
        return builder.build();
    }

    private AsyncHttpClient.BoundRequestBuilder prepareRequest(Command<?> command) {
        String req = Heroku.Config.ENDPOINT.value + command.getEndpoint();
        switch (command.getHttpMethod()) {
            case GET:
                return httpClient.prepareGet(req);
            case POST:
                return httpClient.preparePost(req);
            case PUT:
                return httpClient.preparePut(req);
            case DELETE:
                return httpClient.prepareDelete(req);
            default:
                throw new UnsupportedOperationException(command.getHttpMethod().name() + " is not supported");
        }
    }

    @Override
    public <T> T executeCommand(Command<T> command) {
        try {
            return executeCommandAsync(command).get(30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new HerokuAPIException("request interrupted", e);
        } catch (ExecutionException e) {
            throw new HerokuAPIException("execution exception", e.getCause());
        } catch (TimeoutException e) {
            throw new HerokuAPIException("request timeout after 30 sec", e.getCause());
        }
    }

    @Override
    public <T> ListenableFuture<T> executeCommandAsync(final Command<T> command) {
        Request req = buildRequest(command);
        AsyncCompletionHandler<T> handler = new AsyncCompletionHandler<T>() {
            @Override
            public T onCompleted(Response response) throws Exception {
                return command.getResponse(response.getResponseBody("UTF-8").getBytes(), response.getStatusCode());
            }
        };
        try {
            return httpClient.executeRequest(req, handler);
        } catch (IOException e) {
            throw new HerokuAPIException("IOException while executing request", e);
        }
    }


}
