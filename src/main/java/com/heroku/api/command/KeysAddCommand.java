package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.*;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeysAddCommand implements Command<EmptyResponse> {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

    private final CommandConfig config;

    public KeysAddCommand(CommandConfig config) {
        this.config = config;
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

    @Override
    public int getSuccessCode() {
        return HttpStatus.OK.statusCode;
    }

    @Override
    public EmptyResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}