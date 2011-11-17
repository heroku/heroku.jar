package com.heroku.api.command.app;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.HerokuStack;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCreate implements Command<JsonMapResponse> {

    private final CommandConfig config;

    public AppCreate(String stack) {
        this(HerokuStack.valueOf(stack));
    }

    public AppCreate(HerokuStack stack) {
        config = new CommandConfig().onStack(stack);
    }

    public AppCreate withName(String name) {
        return new AppCreate(config.with(HerokuRequestKey.createAppName, name));
    }

    private AppCreate(CommandConfig config) {
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
        return HttpUtil.encodeParameters(config, HerokuRequestKey.stack, HerokuRequestKey.createAppName);
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
    public JsonMapResponse getResponse(InputStream in, int code) {
        if (code == HttpStatus.ACCEPTED.statusCode)
            return new JsonMapResponse(in);
        else
            throw new RequestFailedException("Failed to create app", code, in);
    }
}
