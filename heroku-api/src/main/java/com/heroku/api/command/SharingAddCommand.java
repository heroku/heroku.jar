package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingAddCommand implements Command<EmptyResponse> {

    // xml(post("/apps/#{app_name}/collaborators", { 'collaborator[email]' => email }).to_s)

    private final CommandConfig config;

    public SharingAddCommand(String appName, String collaboratorEmail) {
        this.config = new CommandConfig().app(appName).with(HerokuRequestKey.collaborator, collaboratorEmail);
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Collaborators.value, config.get(HerokuRequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, HerokuRequestKey.collaborator);
    }

    @Override
    public Accept getResponseType() {
        return Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeader.Util.setHeaders(ContentType.FORM_URLENCODED);
    }

    @Override
    public EmptyResponse getResponse(InputStream in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return new EmptyResponse(in);
        else
            throw new RequestFailedException("SharingAdd failed", code, in);
    }
}
