package com.heroku.api.request.ps;

import com.heroku.api.Heroku;
import com.heroku.api.Proc;
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
// TODO: Currently in test module until fully supported
public class Stop implements Request<Unit> {

    private final RequestConfig config;

    public Stop(String appName, Proc process) {
        config = new RequestConfig().app(appName).with(Heroku.RequestKey.ProcessName, process.getProcess());
    }

    public Stop(String appName, String processType) {
        config = new RequestConfig().app(appName).with(Heroku.RequestKey.ProcessType, processType);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Stop.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.ProcessName, Heroku.RequestKey.ProcessType);
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
    public Unit getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return Unit.unit;
        } else if (status == Http.Status.FORBIDDEN.statusCode) {
            throw HttpUtil.insufficientPrivileges(status, bytes);
        } else {
            throw new RequestFailedException("Error occurred while scaling.", status, bytes);
        }
    }
}
