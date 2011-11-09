package com.heroku.api.connection;

import com.google.gson.Gson;
import com.heroku.api.HerokuApiVersion;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HttpHeader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class BasicAuthConnectionProvider implements ConnectionProvider {
    private final URL endpoint;
    private final String username;
    private final String password;

    public BasicAuthConnectionProvider(String username, String password) {
        this(username, password, ConnectionProvider.DEFAULT_ENDPOINT);
    }

    public BasicAuthConnectionProvider(String username, String password, String endpoint) {
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
    public Connection getConnection() throws IOException {
        HttpPost loginPost = new HttpPost(endpoint.toString() + HerokuResource.Login.value);
        loginPost.addHeader(HttpHeader.Accept.HEADER, HttpHeader.Accept.APPLICATION_JSON);
        loginPost.addHeader(HerokuApiVersion.HEADER, String.valueOf(HerokuApiVersion.v2.version));
        loginPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", password)
        )));

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(loginPost);
        
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String rawLoginResponse = EntityUtils.toString(response.getEntity());
            LoginResponse loginResponse = new Gson().fromJson(rawLoginResponse, LoginResponse.class);
            return new BasicAuthConnection(
                    endpoint,
                    loginResponse.api_key,
                    loginResponse.id,
                    loginResponse.email
            );
        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
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