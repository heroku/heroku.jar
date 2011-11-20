package com.heroku.api.command.login;

import com.heroku.api.Heroku;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

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
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Login.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(
                new CommandConfig().with(Heroku.RequestKey.username, username)
                        .with(Heroku.RequestKey.password, password),
                Heroku.RequestKey.username, Heroku.RequestKey.password
        );
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }


    @Override
    public LoginResponse getResponse(byte[] in, int code) {
        if (code == 200) {
            return new LoginResponse(in);
        } else if (code == 404) {
            throw new RequestFailedException("Invalid username and password combination.", code, in);
        } else {
            throw new RequestFailedException("Unknown error occurred while connecting to Heroku.", code, in);
        }
    }
}

