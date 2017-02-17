package com.heroku.api.request.sharing;

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
public class SharingAdd implements Request<Unit> {

    // xml(post("/apps/#{app_name}/collaborators", { 'collaborator[email]' => email }).to_s)

    private final RequestConfig config;

    public SharingAdd(String appName, String collaboratorEmail) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.Collaborator, collaboratorEmail);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Collaborators.format(config.getAppName());
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
    public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (Http.Status.OK.equals(code) || Http.Status.CREATED.equals(code))
            return Unit.unit;
        else
            throw new RequestFailedException("SharingAdd failed", code, in);
    }
}
