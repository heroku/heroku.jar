package com.heroku.api.request.config;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

import java.util.Collections;
import java.util.Map;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ConfigList implements Request<Map<String, String>> {

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
        return Heroku.Resource.ConfigVars.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw noBody();
    }

    @Override
    public Map<String,Object> getBodyAsMap() {
        throw noBody();
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
    public Map<String, String> getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else if (status == Http.Status.NOT_FOUND.statusCode) {
            throw new RequestFailedException("Application not found.", status, bytes);
        } else if (status == Http.Status.FORBIDDEN.statusCode) {
            throw new RequestFailedException(
                    "Insufficient privileges to \"" + config.getAppName() + "\"",
                    status,
                    bytes
            );
        } else {
            throw new RequestFailedException("Unable to list config failed.", status, bytes);
        }
    }
}
