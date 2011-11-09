package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.HttpHeader;
import com.heroku.api.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeysAddCommand implements Command {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

    private final CommandConfig config;

    public KeysAddCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
    public ResponseType getResponseType() {
        return ResponseType.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>() {{
            put(HttpHeader.ContentType.HEADER, HttpHeader.ContentType.SSH_AUTHKEY);
        }};
    }

    @Override
    public int getSuccessCode() {
        return HttpStatus.OK.statusCode;
    }

    @Override
    public CommandResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}