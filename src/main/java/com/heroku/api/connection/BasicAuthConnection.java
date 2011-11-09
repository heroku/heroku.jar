package com.heroku.api.connection;

import com.heroku.api.HerokuApiVersion;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandResponse;
import com.heroku.api.http.Accept;
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
import java.net.URL;
import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class BasicAuthConnection implements Connection {

    private final URL endpoint;
    private final String apiKey;
    private final String userId;
    private final String userEmail;

    public BasicAuthConnection(URL endpoint, String apiKey, String userId, String userEmail) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.userId = userId;
        this.userEmail = userEmail;
    }
    
    @Override
    public CommandResponse executeCommand(Command command) throws IOException {
        HttpRequestBase message = getHttpRequestBase(command.getHttpMethod(), endpoint + command.getEndpoint());
        message.setHeader(HerokuApiVersion.HEADER, String.valueOf(HerokuApiVersion.v2.version));
        message.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());

        for (Map.Entry<String, String> header : command.getHeaders().entrySet()) {
            message.setHeader(header.getKey(), header.getValue());
        }

        if (command.hasBody()) {
            ((HttpEntityEnclosingRequestBase)message).setEntity(
                    new StringEntity(command.getBody())
            );
        }

        HttpClient client = getHttpClient();
        HttpResponse httpResponse = client.execute(message);

        boolean success = (httpResponse.getStatusLine().getStatusCode() == command.getSuccessCode());

        return command.getResponse(EntityUtils.toByteArray(httpResponse.getEntity()), success);
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
    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                new AuthScope(endpoint.getHost(), endpoint.getPort()),
                // the Basic Authentication scheme only expects an API key.
                new UsernamePasswordCredentials("", apiKey)
        );
        return client;
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
