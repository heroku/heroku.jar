package com.heroku.api.command;


import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StreamCommand implements Command<StreamResponse> {

    public URL stream;

    public StreamCommand(URL toStream) {
        stream = toStream;
    }

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return stream.toString();
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

    @Override
    public StreamResponse getResponse(InputStream inputStream, int status) {
        if (status == 200)
            return new StreamResponse(inputStream);
        else
            throw new RequestFailedException("Unable to open stream", status, inputStream);
    }
}
