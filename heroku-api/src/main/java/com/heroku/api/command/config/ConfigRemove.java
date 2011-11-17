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
public class ConfigRemove implements Command<JsonMapResponse> {

    private final CommandConfig config;

    public ConfigRemove(String appName, String configVarName) {
        config = new CommandConfig().app(appName).with(HerokuRequestKey.configVarName, configVarName);
    }

    @Override
    public Method getHttpMethod() {
        return Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(
                HerokuResource.ConfigVar.value,
                config.get(HerokuRequestKey.appName),
                config.get(HerokuRequestKey.configVarName
        ));
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
        } else {
            throw new RequestFailedException("Config removal failed.", status, bytes);
        }
    }
}
