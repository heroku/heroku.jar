package com.heroku.api.request.app;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.response.JsonMapResponse;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCreate implements Request<App> {

    private final RequestConfig config;

    public AppCreate(String stack) {
        this(Heroku.Stack.valueOf(stack));
    }

    public AppCreate(Heroku.Stack stack) {
        config = new RequestConfig().onStack(stack);
    }

    public AppCreate withName(String name) {
        return new AppCreate(config.with(Heroku.RequestKey.CreateAppName, name));
    }

    private AppCreate(RequestConfig config) {
        this.config = config;
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
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.Stack, Heroku.RequestKey.CreateAppName);
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
    public App getResponse(byte[] in, int code) {
        if (code == Http.Status.ACCEPTED.statusCode)
            return new App(new JsonMapResponse(in));
        else
            throw new RequestFailedException("Failed to create app", code, in);
    }
}
