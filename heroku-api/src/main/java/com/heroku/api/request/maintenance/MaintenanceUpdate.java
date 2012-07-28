package com.heroku.api.request.maintenance;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class MaintenanceUpdate implements Request<Unit> {

    private final RequestConfig config;

    public MaintenanceUpdate(String appName, boolean enable) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.MaintenanceMode, enable ? "1" : "0");
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Maintenance.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.MaintenanceMode);
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
    public Unit getResponse(byte[] in, int code) {
        if (Http.Status.OK.equals(code))
            return Unit.unit;
        else
            throw new RequestFailedException("MaintenanceUpdate failed", code, in);
    }
}
