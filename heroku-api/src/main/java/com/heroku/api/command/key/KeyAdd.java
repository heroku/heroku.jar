package com.heroku.api.command.key;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.EmptyResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeyAdd implements Command<EmptyResponse> {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

    private final CommandConfig config;

    public KeyAdd(String sshkey) {
        this.config = new CommandConfig().with(HerokuRequestKey.sshkey, sshkey);
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return HerokuResource.Keys.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.get(HerokuRequestKey.sshkey);
    }

    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeader.Util.setHeaders(ContentType.SSH_AUTHKEY);
    }

    public EmptyResponse getResponse(byte[] in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return new EmptyResponse();
        else
            throw new RequestFailedException("KeysAdd failed", code, in);
    }

}
