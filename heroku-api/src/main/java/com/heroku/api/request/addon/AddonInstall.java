package com.heroku.api.request.addon;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.request.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonInstall implements Request<JsonMapResponse> {

    private final RequestConfig config;

    public AddonInstall(String appName, String addonName) {
        config = new RequestConfig().app(appName).with(Heroku.RequestKey.AddonName, addonName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
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
        throw HttpUtil.noBody();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }

    @Override
    public JsonMapResponse getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return new JsonMapResponse(bytes);
        } else {
            throw new RequestFailedException("Unable to add addon " + config.get(Heroku.RequestKey.AddonName), status, bytes);
        }
    }
}
