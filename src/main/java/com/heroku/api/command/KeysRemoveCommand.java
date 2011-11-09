package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeysRemoveCommand implements Command {

    // delete("/user/keys/#{escape(key)}").to_s

    private final CommandConfig config;

    public KeysRemoveCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Key.value, config.get(HerokuRequestKey.name));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw new UnsupportedOperationException("This command does not have a body. Use hasBody() to check for a body.");
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
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