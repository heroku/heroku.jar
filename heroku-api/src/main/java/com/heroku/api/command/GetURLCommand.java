package com.heroku.api.command;


import com.heroku.api.http.Accept;
import com.heroku.api.http.Method;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Command for Commands that simply do a get on a URL and do something with the response
 *
 * @param <T> the type of the response, probably TextResponse or StreamResponse
 */
public abstract class GetURLCommand<T extends CommandResponse> implements Command<T> {

    protected URL get;

    public GetURLCommand(URL toGet) {
        get = toGet;
    }

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return get.toString();
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Accept getResponseType() {
        return Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }


}
