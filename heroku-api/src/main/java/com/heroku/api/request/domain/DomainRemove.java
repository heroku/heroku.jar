package com.heroku.api.request.domain;

import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.Heroku.RequestKey.DeleteDomain;
import static com.heroku.api.Heroku.Resource.Domain;
import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class DomainRemove implements Request<Unit> {
    private final RequestConfig config;

    public DomainRemove(String appName, String domainName) {
        this(new RequestConfig().app(appName).with(DeleteDomain, domainName));
    }
    public DomainRemove(RequestConfig config) {
        this.config = config;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Domain.format(config.getAppName(), config.get(DeleteDomain));
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
    public Unit getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (Http.Status.OK.equals(status)) {
            return Unit.unit;
        } else {
            throw new RequestFailedException("Unable to remove domain.", status, bytes);
        }
    }
}
