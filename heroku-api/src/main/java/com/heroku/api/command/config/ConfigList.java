package com.heroku.api.command.config;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ConfigList implements Command<JsonMapResponse> {
    private final CommandConfig config;
    
    public ConfigList(String appName) {
        config = new CommandConfig().app(appName);
    }
    
    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.ConfigVars.value, config.get(HerokuRequestKey.appName));
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
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public JsonMapResponse getResponse(byte[] bytes, int status) {
        if (status == HttpStatus.OK.statusCode) {
            return new JsonMapResponse(bytes);
        } else if (status == HttpStatus.NOT_FOUND.statusCode) {
            throw new RequestFailedException("Application not found.", status, bytes);
        } else if (status == HttpStatus.FORBIDDEN.statusCode) {
            throw new RequestFailedException(
                    "Insufficient privileges to \"" + config.get(HerokuRequestKey.appName) + "\"",
                    status,
                    bytes
            );
        } else {
            throw new RequestFailedException("Unable to list config failed.", status, bytes);
        }
    }
}
