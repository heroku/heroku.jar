package com.heroku.api.command.addon;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonInstall implements Command<JsonMapResponse> {

    private final CommandConfig config;

    public AddonInstall(String appName, String addonName) {
        config = new CommandConfig().app(appName).with(Heroku.RequestKey.addonName, addonName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(Heroku.Resource.AppAddon.value, config.get(Heroku.RequestKey.appName), config.get(Heroku.RequestKey.addonName));
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
            throw new RequestFailedException("Unable to add addon " + config.get(Heroku.RequestKey.addonName), status, bytes);
        }
    }
}
