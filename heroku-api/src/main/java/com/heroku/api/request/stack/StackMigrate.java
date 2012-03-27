package com.heroku.api.request.stack;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

/**
 * @author Naaman Newbold
 */
public class StackMigrate implements Request<String> {
    private final RequestConfig config;

    public StackMigrate(String appName, Heroku.Stack stack) {
        this.config = new RequestConfig().app(appName).onStack(stack);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppStack.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.get(Heroku.RequestKey.Stack);
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
    public String getResponse(byte[] bytes, int status) {
        if (Http.Status.OK.equals(status)) {
            return HttpUtil.getUTF8String(bytes);
        } else {
            throw new RequestFailedException("Unable to migrate stacks.", status, bytes);
        }
    }
}
