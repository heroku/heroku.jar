package com.heroku.api.request.config;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.request.response.JsonMapResponse;
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
public class ConfigList implements Request<JsonMapResponse> {
    private final RequestConfig config;
    
    public ConfigList(String appName) {
        config = new RequestConfig().app(appName);
    }
    
    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.ConfigVars.format(config.get(Heroku.RequestKey.appName));
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
        } else if (status == Http.Status.NOT_FOUND.statusCode) {
            throw new RequestFailedException("Application not found.", status, bytes);
        } else if (status == Http.Status.FORBIDDEN.statusCode) {
            throw new RequestFailedException(
                    "Insufficient privileges to \"" + config.get(Heroku.RequestKey.appName) + "\"",
                    status,
                    bytes
            );
        } else {
            throw new RequestFailedException("Unable to list config failed.", status, bytes);
        }
    }
}
