package com.heroku.api.request.domain;

import com.heroku.api.Domain;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.heroku.api.Heroku.Resource.Domains;
import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class DomainList implements Request<List<Domain>> {
    private final RequestConfig config;

    public DomainList(String appName) {
        this(new RequestConfig().app(appName));
    }

    public DomainList(RequestConfig config) {
        this.config = config;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Domains.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw noBody();
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
    public List<Domain> getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (Http.Status.OK.equals(status)) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to list domains.", status, bytes);
        }
    }
}
