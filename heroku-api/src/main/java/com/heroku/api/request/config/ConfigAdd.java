package com.heroku.api.request.config;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class ConfigAdd implements Request<Unit> {

    private final RequestConfig config;

    public ConfigAdd(String appName, String jsonConfigVars) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.ConfigVars, jsonConfigVars);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.ConfigVars.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.get(Heroku.RequestKey.ConfigVars);
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
    public Unit getResponse(byte[] in, int code) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("Config add failed", code, in);
    }
}