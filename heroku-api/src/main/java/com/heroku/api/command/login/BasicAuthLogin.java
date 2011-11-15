package com.heroku.api.command.login;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.io.InputStream;
import java.util.Map;


public class BasicAuthLogin implements LoginCommand {

    private String username;
    private String password;
    private String endpoint;

    public BasicAuthLogin(String username, String password) {
        this(username, password, DEFAULT_ENDPOINT);
    }

    public BasicAuthLogin(String username, String password, String endpoint) {
        this.username = username;
        this.password = password;
        this.endpoint = endpoint;
    }

    @Override
    public String getApiEndpoint() {
        return endpoint;
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return HerokuResource.Login.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(
                new CommandConfig().with(HerokuRequestKey.username, username)
                        .with(HerokuRequestKey.password, password),
                HerokuRequestKey.username, HerokuRequestKey.password
        );
    }

    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeader.Util.setHeaders(ContentType.FORM_URLENCODED);
    }


    @Override
    public LoginResponse getResponse(InputStream in, int code) {
        if (code == 200) {
            return new LoginResponse(in);
        } else if (code == 404) {
            throw new RequestFailedException("Invalid username and password combination.", code, in);
        } else {
            throw new RequestFailedException("Unknown error occurred while connecting to Heroku.", code, in);
        }
    }
}

