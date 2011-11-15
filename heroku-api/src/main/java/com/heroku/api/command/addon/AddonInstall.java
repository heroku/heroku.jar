package com.heroku.api.command.addon;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonInstall implements Command<JsonMapResponse> {

    private final CommandConfig config;

    public AddonInstall(String appName, String addonName) {
        config = new CommandConfig().app(appName).with(HerokuRequestKey.addonName, addonName);
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.AppAddon.value, config.get(HerokuRequestKey.appName), config.get(HerokuRequestKey.addonName));
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
        return HttpHeader.Util.setHeaders(ContentType.FORM_URLENCODED);
    }

    @Override
    public JsonMapResponse getResponse(InputStream inputStream, int status) {
        if (status == HttpStatus.OK.statusCode) {
            return new JsonMapResponse(inputStream);
        } else {
            throw new RequestFailedException("Unable to add addon " + config.get(HerokuRequestKey.addonName), status, inputStream);
        }
    }
}
