package com.heroku.api.request.addon;

import com.heroku.api.Addon;
import com.heroku.api.AddonChange;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonInstall implements Request<AddonChange> {

    private final RequestConfig config;

    public AddonInstall(String appName, String addonPlan) {
        config = new RequestConfig().app(appName).with(Heroku.RequestKey.AddonPlan, addonPlan);
    }

    public AddonInstall(String appName, String addonPlan, Map<String,String> addonConfig) {
        config = new RequestConfig().app(appName)
            .with(Heroku.RequestKey.AddonPlan, addonPlan)
            .withOptions(Heroku.RequestKey.AddonConfig, addonConfig);
    }

    public AddonInstall(String appName, String addonPlan, final String attachmentName, Map<String,String> addonConfig) {
        config = new RequestConfig().app(appName)
            .with(Heroku.RequestKey.AddonPlan, addonPlan)
            .with(Heroku.RequestKey.AddonAttachment, new HashMap<Heroku.RequestKey, RequestConfig.Either>() {{
                put(Heroku.RequestKey.AddonAttachmentName, new RequestConfig.Either(attachmentName));
            }})
            .withOptions(Heroku.RequestKey.AddonConfig, addonConfig);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppAddons.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.asJson();
    }

    @Override
    public Map<String,Object> getBodyAsMap() {
        return config.asMap();
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
        if (status == Http.Status.CREATED.statusCode) {
            return Json.parse(bytes, this.getClass());
        } else {
            throw new RequestFailedException("Unable to add addon " + config.get(Heroku.RequestKey.AddonPlan), status, bytes);
        }
    }
}
