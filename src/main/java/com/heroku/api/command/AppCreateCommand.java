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
 * @author Naaman Newbold
 */
public class AppCreateCommand implements Command {

    private final CommandConfig config;

    public AppCreateCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
    public ResponseType getResponseType() {
        return ResponseType.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>() {{
            put(HttpHeader.ContentType.HEADER, HttpHeader.ContentType.FORM_URLENCODED);
        }};
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
