package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.*;

import java.util.Map;


public class BasicAuthLoginCommand implements LoginCommand {

    String username;
    String password;
    String endpoint;

    public BasicAuthLoginCommand(String username, String password) {
        this(username, password, DEFAULT_ENDPOINT);
    }

    public BasicAuthLoginCommand(String username, String password, String endpoint) {
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
        return HttpUtil.encodeParameters(new CommandConfig().with(HerokuRequestKey.username, username).with(HerokuRequestKey.password, password), HerokuRequestKey.username, HerokuRequestKey.password);
    }

    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeader.Util.setHeaders(HerokuApiVersion.v2, ContentType.FORM_URLENCODED);
    }

    @Override
    public int getSuccessCode() {
        return 200;
    }

    @Override
    public LoginResponse getResponse(byte[] bytes, boolean success) {
        return new LoginResponse(bytes, success);
    }
}


