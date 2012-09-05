package com.heroku.api.request.sharing;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

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
        return Heroku.Resource.Collaborators.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.Collaborator);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }

    @Override
    public Unit getResponse(byte[] in, int code) {
        if (Http.Status.OK.equals(code) || Http.Status.CREATED.equals(code))
            return Unit.unit;
        else
            throw new RequestFailedException("SharingAdd failed", code, in);
    }
}
