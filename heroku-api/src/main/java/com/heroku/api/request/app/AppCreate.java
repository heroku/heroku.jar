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
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCreate implements Request<App> {

    private final RequestConfig config;

    public AppCreate(App app) {
        RequestConfig builder = new RequestConfig();
        builder = (app.getName() != null) ? builder.with(Heroku.RequestKey.AppName, app.getName()) : builder;
        builder = (app.getStack() != null) ? builder.with(Heroku.RequestKey.Stack, app.getStack().getName()) : builder;
        config = builder;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Apps.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.asJson();
    }

    @Override
    public Map<String,Object> getBodyAsMap() {
        return config.asMap();
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
    public App getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.CREATED.statusCode)
            return parse(in, getClass());
        else if (code == Http.Status.ACCEPTED.statusCode)
            return parse(in, getClass());
        else
            throw new RequestFailedException("Failed to create app", code, in);
    }
}
