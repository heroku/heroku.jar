package com.heroku.connection;

import com.google.gson.Gson;
import com.heroku.HerokuResource;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuBasicAuthConnectionProvider implements HerokuConnectionProvider {
    private final URL endpoint;
    private final String username;
    private final String password;

    public HerokuBasicAuthConnectionProvider(String username, String password) throws HerokuAPIException {
        this(username, password, HerokuConnectionProvider.DEFAULT_ENDPOINT);
    }

    public HerokuBasicAuthConnectionProvider(String username, String password, String endpoint) throws HerokuAPIException {
        super();
        this.username = username;
        this.password = password;
        try {
            this.endpoint = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new HerokuAPIException("Invalid endpoint: " + endpoint, e);
        }
    }

    @Override
    public HerokuConnection getConnection() throws HerokuAPIException, IOException {
        PostMethod loginPost = new PostMethod(endpoint.toString() + HerokuResource.LOGIN.value);
        loginPost.addRequestHeader(new Header("Accept", "application/json"));
        loginPost.addRequestHeader(new Header("X-Heroku-API-Version", "2"));
        loginPost.setRequestBody(new NameValuePair[] {
                new NameValuePair("username", username),
                new NameValuePair("password", password)
        });

        HttpClient client = new HttpClient();
        client.executeMethod(loginPost);
        
        if (loginPost.getStatusCode() == HttpStatus.SC_OK) {
            String rawLoginResponse = new String(loginPost.getResponseBody());
            LoginResponse loginResponse = new Gson().fromJson(rawLoginResponse, LoginResponse.class);
            return new HerokuBasicAuthConnection(
                    endpoint,
                    loginResponse.api_key,
                    loginResponse.id,
                    loginResponse.email
            );
        } else if (loginPost.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
            throw new HerokuAPIException("Invalid username and password combination.");
        } else {
            throw new HerokuAPIException("Unknown error occurred while connecting to Heroku.");
        }
    }


    public static class LoginResponse {
        public String api_key;
        public String verified_at;
        public String id;
        public String email;
    }
}