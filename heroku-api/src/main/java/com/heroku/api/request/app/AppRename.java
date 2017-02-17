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
public class AppRename implements Request<App> {
    private final RequestConfig config;

    public AppRename(String appName, String newName) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.AppName, newName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.App.format(config.getAppName());
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
    public App getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        }
        throw new RequestFailedException("Unable to renameApp application.", status, bytes);
    }
}
