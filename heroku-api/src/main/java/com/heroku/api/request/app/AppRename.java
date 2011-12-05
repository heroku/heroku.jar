package com.heroku.api.request.app;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.model.App;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

import static com.heroku.api.http.HttpUtil.encodeParameters;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppRename implements Request<App> {
    private final RequestConfig config;

    public AppRename(String appName, String newName) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.CreateAppName, newName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.App.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return encodeParameters(config, Heroku.RequestKey.CreateAppName);
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
    public App getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        }
        throw new RequestFailedException("Unable to rename application.", status, bytes);
    }
}
