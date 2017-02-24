package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.HashMap;
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
        builder = (app.getStack() != null) ? builder.onStack(app.getStack()) : builder;

        if (app.getSpace() != null) {
            Map<Heroku.RequestKey, RequestConfig.Either> space = new HashMap<>();
            space.put(Heroku.RequestKey.SpaceId, new RequestConfig.Either(app.getSpace().getId()));
            space.put(Heroku.RequestKey.SpaceName, new RequestConfig.Either(app.getSpace().getName()));
            space.put(Heroku.RequestKey.SpaceShield, new RequestConfig.Either(app.getSpace().getShield()));

            builder.with(Heroku.RequestKey.Space, space);
        }

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
