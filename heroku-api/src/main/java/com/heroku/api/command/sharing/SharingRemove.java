package com.heroku.api.command.sharing;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.Unit;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

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
        this.config = new CommandConfig().app(appName).with(Heroku.RequestKey.collaborator, collaboratorEmail);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Collaborator.format(config.get(Heroku.RequestKey.appName),
                HttpUtil.urlencode(config.get(Heroku.RequestKey.collaborator), "Unable to encode the endpoint"));
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
        return Http.Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public Unit getResponse(byte[] in, int code) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("SharingRemove failed", code, in);
    }
}