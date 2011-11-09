package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.*;
import com.heroku.api.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingAddCommand implements Command {

    // xml(post("/apps/#{app_name}/collaborators", { 'collaborator[email]' => email }).to_s)

    private final CommandConfig config;

    public SharingAddCommand(CommandConfig config) {
        this.config = config;
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
        try {
            return HttpUtil.encodeParameters(config, HerokuRequestKey.collaborator);
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Unable to encode parameters", e);
        }
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
    public CommandResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}
