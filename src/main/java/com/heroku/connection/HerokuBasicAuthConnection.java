package com.heroku.connection;

import com.heroku.command.HerokuCommand;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import java.io.IOException;
import java.net.URL;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuBasicAuthConnection implements HerokuConnection {

    private final URL endpoint;
    private final String apiKey;
    private final String userId;
    private final String userEmail;

    public HerokuBasicAuthConnection(URL endpoint, String apiKey, String userId, String userEmail) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    @Override
    public void executeCommand(HerokuCommand command) throws HerokuAPIException, IOException {
        command.execute(this);
    }

    @Override
    public HttpClient getHttpClient() {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(
                new AuthScope(endpoint.getHost(), endpoint.getPort()),
                new UsernamePasswordCredentials(userEmail, apiKey)
        );
        return client;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends HttpMethod> T getHttpMethod(T method) {
        if (method == null)
            throw new IllegalArgumentException("The HttpMethod cannot be null.");

        method.addRequestHeader("X-Heroku-API-Version", "2");
        return method;
    }

    @Override
    public URL getEndpoint() {
        return endpoint;
    }

    @Override
    public String getEmail() {
        return userEmail;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
