package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.HerokuStack;
import com.heroku.api.http.*;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCreateCommand implements Command<JsonMapResponse> {

    private final CommandConfig config;
    
    public AppCreateCommand(String stack) {
        config = new CommandConfig().onStack(HerokuStack.valueOf(stack));
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

        return HttpUtil.encodeParameters(config,
                HerokuRequestKey.stack
        );

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
    public JsonMapResponse getResponse(byte[] bytes, boolean success) {
        return new JsonMapResponse(bytes, success);
    }
}
