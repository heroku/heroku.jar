package com.heroku.api.command.app;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCreate implements Command<JsonMapResponse> {

    private final CommandConfig config;

    public AppCreate(String stack) {
        this(Heroku.Stack.valueOf(stack));
    }

    public AppCreate(Heroku.Stack stack) {
        config = new CommandConfig().onStack(stack);
    }

    public AppCreate withName(String name) {
        return new AppCreate(config.with(Heroku.RequestKey.createAppName, name));
    }

    private AppCreate(CommandConfig config) {
        this.config = config;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Apps.value;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.stack, Heroku.RequestKey.createAppName);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }

    @Override
    public JsonMapResponse getResponse(byte[] in, int code) {
        if (code == Http.Status.ACCEPTED.statusCode)
            return new JsonMapResponse(in);
        else
            throw new RequestFailedException("Failed to create app", code, in);
    }
}
