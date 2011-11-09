package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.Method;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingRemoveCommand implements Command {

    // delete("/apps/#{app_name}/collaborators/#{escape(email)}").to_s

    private final CommandConfig config;

    public SharingRemoveCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public Method getHttpMethod() {
        return Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        try {
            return String.format(HerokuResource.Collaborator.value,
                            config.get(HerokuRequestKey.name),
                            URLEncoder.encode(config.get(HerokuRequestKey.collaborator), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Unable to encode the endpoint.", e);
        }
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw new UnsupportedOperationException("This command does not have a body. Use hasBody() to check for a body.");
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
    public int getSuccessCode() {
        return HttpStatus.OK.statusCode;
    }

    @Override
    public CommandResponse getResponse(byte[] bytes, boolean success) {
        return new EmptyResponse(success);
    }
}