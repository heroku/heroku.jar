package com.heroku.api.request.login;

import com.heroku.api.Heroku;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.request.LoginRequest;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;


public class BasicAuthLogin implements LoginRequest {

    private String username;
    private String password;

    public BasicAuthLogin(String username, String password) {
        this.username = username;
        this.password = password;
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
                new RequestConfig().with(Heroku.RequestKey.Username, username)
                        .with(Heroku.RequestKey.Password, password),
                Heroku.RequestKey.Username, Heroku.RequestKey.Password
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

