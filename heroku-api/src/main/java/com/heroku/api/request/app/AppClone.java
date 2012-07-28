package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Ryan Brainard
 */
public class AppClone implements Request<App> {

    private final RequestConfig config;
    
    public AppClone(String templateAppName, App targetApp) {
        RequestConfig builder = new RequestConfig();
        builder = builder.with(Heroku.RequestKey.AppName, templateAppName);
        builder = (targetApp.getName() != null) ? builder.with(Heroku.RequestKey.CreateAppName, targetApp.getName()) : builder;
        config = builder;
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
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.CreateAppName);
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
    public App getResponse(byte[] in, int status) {
        if (status != Http.Status.OK.statusCode) {
            throw new RequestFailedException("Failed to clone app", status, in);
        }

        return parse(in, getClass());
    }
}
