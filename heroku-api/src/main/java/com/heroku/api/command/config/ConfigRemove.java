package com.heroku.api.command.config;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

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
        config = new CommandConfig().app(appName).with(Heroku.RequestKey.configVarName, configVarName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(
                Heroku.Resource.ConfigVar.value,
                config.get(Heroku.RequestKey.appName),
                config.get(Heroku.RequestKey.configVarName
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
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public JsonMapResponse getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return new JsonMapResponse(bytes);
        } else {
            throw new RequestFailedException("Config removal failed.", status, bytes);
        }
    }
}
