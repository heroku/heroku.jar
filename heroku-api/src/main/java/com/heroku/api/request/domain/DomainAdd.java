package com.heroku.api.request.domain;

import com.heroku.api.Domain;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.Heroku.RequestKey.CreateDomain;
import static com.heroku.api.Heroku.Resource.Domains;
import static com.heroku.api.parser.Json.parse;

/**
 * Add a domain to an app.
 *
 * @author Naaman Newbold
 */
public class DomainAdd implements Request<Domain> {

    private final RequestConfig config;

    public DomainAdd(String appName, String domainName) {
        this(new RequestConfig().app(appName).
            with(CreateDomain, domainName));
    }

    public DomainAdd(RequestConfig config) {
        this.config = config;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Domains.format(config.getAppName());
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
    public Domain getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (Http.Status.CREATED.equals(status)) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to add domain.", status, bytes);
        }
    }
}
