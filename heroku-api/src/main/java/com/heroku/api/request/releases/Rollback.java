package com.heroku.api.request.releases;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Rollback implements Request<String> {
    private final RequestConfig config;

    public Rollback(String appName, String releaseName) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.Rollback, releaseName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Releases.format(config.get(Heroku.RequestKey.AppName), config.get(Heroku.RequestKey.Rollback));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.Rollback);
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
    public String getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return new String(bytes);
        }
        throw new RequestFailedException("Unable to rollback.", status, bytes);
    }
}
