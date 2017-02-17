package com.heroku.api.request.addon;

import com.heroku.api.AddonChange;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonRemove implements Request<AddonChange> {
    private final RequestConfig config;

    public AddonRemove(String appName, String addonName) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.AddonName, addonName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppAddon.format(config.get(Heroku.RequestKey.AppName), config.get(Heroku.RequestKey.AddonName));
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
    public AddonChange getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        }
        throw new RequestFailedException("Unable to remove addon.", status, bytes);
    }
}
