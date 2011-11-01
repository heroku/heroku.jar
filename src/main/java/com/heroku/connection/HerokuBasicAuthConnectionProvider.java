package com.heroku.connection;

import com.google.gson.Gson;
import com.heroku.HerokuApiVersion;
import com.heroku.HerokuResource;
import com.heroku.command.HerokuResponseFormat;
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
        HttpPost loginPost = new HttpPost(endpoint.toString() + HerokuResource.Login.value);
        loginPost.addHeader(HerokuResponseFormat.JSON.acceptHeader);
        loginPost.addHeader(HerokuApiVersion.v2.versionHeader);
        loginPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", password)
        )));

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(loginPost);
        
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String rawLoginResponse = EntityUtils.toString(response.getEntity());
            LoginResponse loginResponse = new Gson().fromJson(rawLoginResponse, LoginResponse.class);
            return new HerokuBasicAuthConnection(
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