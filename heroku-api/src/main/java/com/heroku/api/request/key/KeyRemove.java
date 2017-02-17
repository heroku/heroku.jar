package com.heroku.api.request.key;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.encodeIncludingSpecialCharacters;
import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeyRemove implements Request<Unit> {

    // delete("/user/keys/#{escape(key)}").to_s

    private final RequestConfig config;

    public KeyRemove(String keyIdOrFingerprint) {
        this.config = new RequestConfig().with(Heroku.RequestKey.SSHKey, keyIdOrFingerprint);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Key.format(encodeIncludingSpecialCharacters(config.get(Heroku.RequestKey.SSHKey)));
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
    public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("KeysRemove failed", code, in);
    }
}