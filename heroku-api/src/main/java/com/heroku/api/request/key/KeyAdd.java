package com.heroku.api.request.key;

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
public class KeyAdd implements Request<Unit> {

    private final RequestConfig config;

    public KeyAdd(String sshkey) {
        this.config = new RequestConfig().with(Heroku.RequestKey.SSHKey, sshkey);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Keys.value;
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

    public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.CREATED.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("KeysAdd failed", code, in);
    }

}
