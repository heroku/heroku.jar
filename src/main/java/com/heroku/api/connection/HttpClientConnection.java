package com.heroku.api.connection;

import com.heroku.api.command.Command;
import com.heroku.api.command.CommandResponse;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.command.LoginResponse;
import com.heroku.api.http.HerokuApiVersion;
import com.heroku.api.http.Method;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HttpClientConnection implements Connection {

    private LoginCommand loginCommand;
    private LoginResponse loginResponse;
    private URL endpoint;
    private boolean loggedIn = false;


    public HttpClientConnection(LoginCommand login) {
        this.loginCommand = login;
        try {
            this.endpoint = new URL(loginCommand.getApiEndpoint());
        } catch (MalformedURLException e) {
            throw new RuntimeException("The endpoint URL was malformed", e);
        }
        loginResponse = executeCommand(loginCommand);
        if (loginResponse.isSuccess()) {
            loggedIn = true;
        }
    }


    @Override
    public <T extends CommandResponse> T executeCommand(Command<T> command) {
        try {
            HttpRequestBase message = getHttpRequestBase(command.getHttpMethod(), endpoint + command.getEndpoint());
            message.setHeader(HerokuApiVersion.HEADER, String.valueOf(HerokuApiVersion.v2.version));
            message.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());

            for (Map.Entry<String, String> header : command.getHeaders().entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            if (command.hasBody()) {
                ((HttpEntityEnclosingRequestBase) message).setEntity(
                        new StringEntity(command.getBody())
                );
            }

            HttpClient client = getHttpClient();
            HttpResponse httpResponse = client.execute(message);

            boolean success = (httpResponse.getStatusLine().getStatusCode() == command.getSuccessCode());

            return command.getResponse(EntityUtils.toByteArray(httpResponse.getEntity()), success);
        } catch (IOException e) {
            throw new RuntimeException("exception while executing command", e);
        }
    }


    private HttpRequestBase getHttpRequestBase(Method httpMethod, String endpoint) {
        switch (httpMethod) {
            case GET:
                return new HttpGet(endpoint);
            case PUT:
                return new HttpPut(endpoint);
            case POST:
                return new HttpPost(endpoint);
            case DELETE:
                return new HttpDelete(endpoint);
            default:
                throw new UnsupportedOperationException(httpMethod + " is not a support request type.");
        }
    }


    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        if (loggedIn) {
            client.getCredentialsProvider().setCredentials(
                    new AuthScope(endpoint.getHost(), endpoint.getPort()),
                    // the Basic Authentication scheme only expects an API key.
                    new UsernamePasswordCredentials("", loginResponse.api_key())
            );
        }
        return client;
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
}
