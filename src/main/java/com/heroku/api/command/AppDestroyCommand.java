package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppDestroyCommand implements Command<EmptyResponse> {

    private final CommandConfig config;

    public AppDestroyCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public Method getHttpMethod() {
        return Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.App.value, config.get(HerokuRequestKey.name));
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
    public Accept getResponseType() {
        return Accept.JSON;
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
    public EmptyResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}
