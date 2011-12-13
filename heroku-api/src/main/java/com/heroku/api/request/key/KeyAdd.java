package com.heroku.api.request.key;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeyAdd implements Request<Unit> {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

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
        return config.get(Heroku.RequestKey.SSHKey);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.SSH_AUTHKEY);
    }

    public Unit getResponse(byte[] in, int code) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("KeysAdd failed", code, in);
    }

}
