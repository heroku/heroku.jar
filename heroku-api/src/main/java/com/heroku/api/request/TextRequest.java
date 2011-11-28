package com.heroku.api.request;

import com.heroku.api.request.response.TextResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TextRequest implements Request<TextResponse> {
    protected URL get;

    public TextRequest(URL toGet) {
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
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
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
    public Http.Accept getResponseType() {
        return Http.Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }
}
