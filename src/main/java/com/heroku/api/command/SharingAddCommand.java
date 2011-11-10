package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.http.*;
import com.heroku.api.http.HttpUtil;

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
        return String.format(HerokuResource.Collaborators.value, config.get(HerokuRequestKey.name));
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
    public int getSuccessCode() {
        return HttpStatus.OK.statusCode;
    }

    @Override
    public EmptyResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}
