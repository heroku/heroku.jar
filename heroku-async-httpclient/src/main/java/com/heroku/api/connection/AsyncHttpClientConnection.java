package com.heroku.api.connection;

import com.heroku.api.HerokuAPI;
import com.heroku.api.command.Command;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.command.login.LoginResponse;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HerokuApiVersion;
import com.heroku.api.http.HttpUtil;
import com.ning.http.client.*;
import com.ning.http.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class AsyncHttpClientConnection implements Connection<ListenableFuture<?>> {

    private LoginCommand loginCommand;
    private LoginResponse loginResponse;
    private URL endpoint;
    private AsyncHttpClient httpClient;


    public AsyncHttpClientConnection(LoginCommand login) {
        this.loginCommand = login;
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        httpClient = new AsyncHttpClient(builder.build());
        this.endpoint = HttpUtil.toURL(loginCommand.getApiEndpoint());
        this.loginResponse = executeCommand(loginCommand);
    }

    private Request buildRequest(Command<?> command) {
        AsyncHttpClient.BoundRequestBuilder builder = prepareRequest(command);
        builder.setHeader(HerokuApiVersion.HEADER, String.valueOf(HerokuApiVersion.v2.version));
        builder.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());
        if (loginResponse != null) {
            try {
                builder.setHeader("Authorization", "Basic " + Base64.encode((":" + loginResponse.api_key()).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new HerokuAPIException("Unupported op exception while encoding api key");
            }
        }
        if (command.hasBody()) {
            builder.setBody(command.getBody());
        }
        return builder.build();
    }

    private AsyncHttpClient.BoundRequestBuilder prepareRequest(Command<?> command) {
        String req = getCommandEndpoint(endpoint, command.getEndpoint()).toString();
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

    @Override
    public HerokuAPI getApi() {
        return new HerokuAPI(this);
    }

    @Override
    public URL getEndpoint() {
        return endpoint;
    }

    @Override
    public String getEmail() {
        return loginResponse.email();
    }

    @Override
    public String getApiKey() {
        return loginResponse.api_key();
    }

    private static URL getCommandEndpoint(URL connectionEndpoint, String commandEndpoint) {
        if (commandEndpoint.startsWith("http://") || commandEndpoint.startsWith("https://")) {
            return HttpUtil.toURL(commandEndpoint);
        } else {
            return HttpUtil.toURL(connectionEndpoint.toString() + commandEndpoint);
        }

    }

}
