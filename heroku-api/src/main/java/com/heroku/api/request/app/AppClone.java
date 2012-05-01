package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Ryan Brainard
 */
public class AppClone implements Request<App> {

    private final RequestConfig config;
    
    public AppClone(String appName) {
        config = new RequestConfig().with(Heroku.RequestKey.AppName, appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppClone.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public App getResponse(byte[] in, int status) {
        if (status != Http.Status.OK.statusCode) {
            throw new RequestFailedException("Failed to clone app", status, in);
        }

        return parse(in, getClass());
    }
}
