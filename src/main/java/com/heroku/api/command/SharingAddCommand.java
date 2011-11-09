package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HttpHeader;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
    public ResponseType getResponseType() {
        return ResponseType.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>(){{
            put(HttpHeader.ContentType.HEADER, HttpHeader.ContentType.FORM_URLENCODED);
        }};
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
