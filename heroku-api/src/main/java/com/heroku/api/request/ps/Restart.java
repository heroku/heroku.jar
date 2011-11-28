package com.heroku.api.request.ps;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.request.response.Unit;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Restart implements Request<Unit> {

    private final RequestConfig config;

    public Restart(String appName) {
        this(new RequestConfig().app(appName));
    }

    Restart(RequestConfig config) {
        this.config = config;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Restart.format(config.get(Heroku.RequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.processName, Heroku.RequestKey.processType);
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
        } else {
            throw new RequestFailedException("Unable to restart the process.", status, bytes);
        }
    }

    public static class NamedProcessRestart extends Restart {
        public NamedProcessRestart(String appName, String processName) {
            super(new RequestConfig().app(appName).with(Heroku.RequestKey.processName, processName));
        }
    }

    public static class ProcessTypeRestart extends Restart {
        public ProcessTypeRestart(String appName, String processType) {
            super(new RequestConfig().app(appName).with(Heroku.RequestKey.processType, processType));
        }
    }
}
