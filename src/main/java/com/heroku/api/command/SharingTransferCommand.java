package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.HerokuAPIException;
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
public class SharingTransferCommand implements Command {

    // heroku.update(app, :transfer_owner => email)
    // put("/apps/#{name}", :app => attributes).to_s
    // http request body = app[transfer_owner]=jw%2Bdemo%40heroku.com

    private final CommandConfig config;

    public SharingTransferCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.App.value, config.get(HerokuRequestKey.name));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        try {
            return HttpUtil.encodeParameters(config, HerokuRequestKey.transferOwner);
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Unable to encode parameters.", e);
        }
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.XML;
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