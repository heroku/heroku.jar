package com.heroku.api.request.releases;

import com.heroku.api.Heroku;
import com.heroku.api.Release;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Rollback implements Request<Release> {
    private final RequestConfig config;

    public Rollback(String appName, String releaseName) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.Release, releaseName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Releases.format(config.getAppName(), config.get(Heroku.RequestKey.Release));
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
    public Release getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.CREATED.statusCode) {
            return parse(bytes, getClass());
        }
        throw new RequestFailedException("Unable to rollback.", status, bytes);
    }
}
