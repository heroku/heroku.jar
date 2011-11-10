package com.heroku.api.connection;

import com.heroku.api.command.Command;
import com.heroku.api.command.CommandResponse;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.command.LoginResponse;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HerokuApiVersion;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
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
    private DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());


    public HttpClientConnection(LoginCommand login) {
        this.loginCommand = login;
        this.endpoint = HttpUtil.toURL(loginCommand.getApiEndpoint());
        this.loginResponse = executeCommand(loginCommand);
        if (loginResponse.isSuccess()) {
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(endpoint.getHost(), endpoint.getPort()),
                    // the Basic Authentication scheme only expects an API key.
                    new UsernamePasswordCredentials("", loginResponse.api_key()));
        } else {
            throw new HerokuAPIException("Unable to login");
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
                ((HttpEntityEnclosingRequestBase) message).setEntity(new StringEntity(command.getBody()));
            }

            HttpResponse httpResponse = httpClient.execute(message);

            return command.getResponse(EntityUtils.toByteArray(httpResponse.getEntity()), httpResponse.getStatusLine().getStatusCode());
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
