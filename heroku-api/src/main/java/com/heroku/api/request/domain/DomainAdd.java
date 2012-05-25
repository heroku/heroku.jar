package com.heroku.api.request.domain;

import com.heroku.api.Domain;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

import static com.heroku.api.Heroku.RequestKey.AppName;
import static com.heroku.api.Heroku.RequestKey.CreateDomain;
import static com.heroku.api.Heroku.Resource.Domains;
import static com.heroku.api.http.Http.ContentType.FORM_URLENCODED;
import static com.heroku.api.http.Http.Header;
import static com.heroku.api.parser.Json.parse;

/**
 * Add a domain to an app.
 *
 * @author Naaman Newbold
 */
public class DomainAdd implements Request<Domain> {
    private final RequestConfig config;

    public DomainAdd(String appName, String domainName) {
        this(new RequestConfig().app(appName).with(CreateDomain, domainName));
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
        return Domains.format(config.get(AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, AppName, CreateDomain);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Header.Util.setHeaders(FORM_URLENCODED);
    }

    @Override
    public Domain getResponse(byte[] bytes, int status) {
        if (Http.Status.CREATED.equals(status)) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to add domain.", status, bytes);
        }
    }
}
