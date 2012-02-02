package com.heroku.api.request.config;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ConfigRemove implements Request<Map<String, String>> {

    private final RequestConfig config;

    public ConfigRemove(String appName, String configVarName) {
        config = new RequestConfig().app(appName).with(Heroku.RequestKey.ConfigVarName, configVarName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.ConfigVar.format(config.get(Heroku.RequestKey.AppName), config.get(Heroku.RequestKey.ConfigVarName));
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
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Config removal failed.", status, bytes);
        }
    }
}
