package com.heroku.api.request.config;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
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
public class ConfigUpdate implements Request<Unit> {

    private final RequestConfig config;

    private final Map<String,?> configVars;

    public ConfigUpdate(String appName, Map<String, String> configVars) {
        this.configVars = configVars;
        this.config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.PATCH;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.ConfigVars.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return Json.encode(configVars);
    }

    @Override
    public Map<String,?> getBodyAsMap() {
        return configVars;
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
            throw new RequestFailedException("Config add failed", code, in);
    }
}