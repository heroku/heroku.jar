package com.heroku.api.request.app;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppDestroy implements Request<Unit> {

    private final RequestConfig config;

    public AppDestroy(String appName) {
        this.config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.App.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw HttpUtil.noBody();
    }

    @Override
    public Map<String,Object> getBodyAsMap() {
        throw noBody();
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
    public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("AppDestroy failed", code, in);
    }
}
