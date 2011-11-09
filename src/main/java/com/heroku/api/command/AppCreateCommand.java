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
 * @author Naaman Newbold
 */
public class AppCreateCommand implements Command {

    private final CommandConfig config;

    public AppCreateCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return HerokuResource.Apps.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        try {
            return HttpUtil.encodeParameters(config,
                    HerokuRequestKey.stack,
                    HerokuRequestKey.remote,
                    HerokuRequestKey.timeout,
                    HerokuRequestKey.addons
            );
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Unable to encode parameters.", e);
        }
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
    public int getSuccessCode() {
        return HttpStatus.ACCEPTED.statusCode;
    }

    @Override
    public CommandResponse getResponse(byte[] bytes, boolean success) {
        return new JsonMapResponse(bytes, success);
    }
}
