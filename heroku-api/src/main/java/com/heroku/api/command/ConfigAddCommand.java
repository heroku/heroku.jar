package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class ConfigAddCommand implements Command<EmptyResponse> {

    // put("/apps/#{app_name}/config_vars", json_encode(new_vars)).to_s

    private final CommandConfig config;

    public ConfigAddCommand(String appName, String jsonConfigVars) {
        this.config = new CommandConfig().app(appName).with(HerokuRequestKey.configvars, jsonConfigVars);
    }

    @Override
    public Method getHttpMethod() {
        return Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.ConfigVars.value, config.get(HerokuRequestKey.name));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.get(HerokuRequestKey.configvars);
    }

    @Override
    public Accept getResponseType() {
        return Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public EmptyResponse getResponse(InputStream in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return new EmptyResponse(in);
        else
            throw new RequestFailedException("AppDestroy failed", code, in);
    }
}