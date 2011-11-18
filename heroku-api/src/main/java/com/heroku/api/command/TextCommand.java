package com.heroku.api.command;

import com.heroku.api.command.response.TextResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.Method;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TextCommand implements Command<TextResponse> {
    protected URL get;

    public TextCommand(URL toGet) {
        this.get = toGet;
    }

    @Override
    public TextResponse getResponse(byte[] bytes, int status) {
        if (status == 200)
            return new TextResponse(bytes);
        else
            throw new RequestFailedException("Unable to open stream", status, bytes);
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
