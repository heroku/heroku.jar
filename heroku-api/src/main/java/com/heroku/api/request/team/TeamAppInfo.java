package com.heroku.api.request.team;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.TeamApp;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Joe Kutner
 */
public class TeamAppInfo implements Request<TeamApp> {

    private final RequestConfig config;

    public TeamAppInfo(String appName) {
        this.config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.TeamApp.format(config.getAppName());
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
    public TeamApp getResponse(byte[] data, int code, Map<String,String> responseHeaders) {
        if (Http.Status.OK.equals(code)) {
            return parse(data, getClass());
        }
        throw new RequestFailedException("Unable to get team app info", code, data);
    }
}
