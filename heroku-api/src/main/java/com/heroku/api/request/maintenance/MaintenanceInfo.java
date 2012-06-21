package com.heroku.api.request.maintenance;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.parser.TypeReference;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class MaintenanceInfo implements Request<Boolean> {

    private final RequestConfig config;

    public MaintenanceInfo(String appName) {
        this.config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Maintenance.format(config.get(Heroku.RequestKey.AppName));
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
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public Boolean getResponse(byte[] in, int code) {
        if (Http.Status.OK.equals(code)) {
            final Map<String, String> parsed = Json.parse(in, new TypeReference<Map<String, String>>() {}.getType());
            return Boolean.valueOf(parsed.get("maintenance"));
        }
        else
            throw new RequestFailedException("MaintenanceInfo failed", code, in);
    }
}
