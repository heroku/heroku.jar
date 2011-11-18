package com.heroku.api.command.sharing;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.Unit;
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
 * @author James Ward
 */
public class SharingRemove implements Command<Unit> {

    // delete("/apps/#{app_name}/collaborators/#{escape(email)}").to_s

    private final CommandConfig config;

    public SharingRemove(String appName, String collaboratorEmail) {
        this.config = new CommandConfig().app(appName).with(HerokuRequestKey.collaborator, collaboratorEmail);
    }

    @Override
    public Method getHttpMethod() {
        return Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Collaborator.value,
                config.get(HerokuRequestKey.appName),
                HttpUtil.urlencode(config.get(HerokuRequestKey.collaborator), "Unable to encode the endpoint"));
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
        return Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public Unit getResponse(byte[] in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("SharingRemove failed", code, in);
    }
}