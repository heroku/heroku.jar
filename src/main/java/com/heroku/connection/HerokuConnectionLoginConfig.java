package com.heroku.connection;

import com.google.gson.Gson;
import com.heroku.HerokuResource;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuConnectionLoginConfig extends HerokuConnectionConfig {
    private final String endpoint;
    private final String username;
    private final String password;

    public HerokuConnectionLoginConfig(String username, String password, String endpoint) {
        super();
        this.username = username;
        this.password = password;
        this.endpoint = endpoint;
    }

    @Override
    public HerokuConnection connect() throws IOException {
        PostMethod loginPost = new PostMethod(endpoint + HerokuResource.LOGIN.value);
        loginPost.addRequestHeader(new Header("Accept", "application/json"));
        loginPost.addRequestHeader(new Header("X-Heroku-API-Version", "2"));
        loginPost.setRequestBody(new NameValuePair[] {
                new NameValuePair("username", username),
                new NameValuePair("password", password)
        });
        HttpClient client = new HttpClient();
        
        client.executeMethod(loginPost);
        String rawLoginResponse = new String(loginPost.getResponseBody());

        LoginResponse loginResponse = new Gson().fromJson(rawLoginResponse, LoginResponse.class);
        return new HerokuBasicAuthConnection(
                endpoint,
                loginResponse.api_key,
                loginResponse.id,
                loginResponse.email
        );
    }


    public static class LoginResponse {
        public String api_key;
        public String verified_at;
        public String id;
        public String email;
    }
}