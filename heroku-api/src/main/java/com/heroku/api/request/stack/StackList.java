package com.heroku.api.request.stack;

import com.heroku.api.Heroku;
import com.heroku.api.StackInfo;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * @author Naaman Newbold
 */
public class StackList implements Request<List<StackInfo>>{
    private final RequestConfig config;

    public StackList(String appName) {
        this.config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppStack.format(config.get(Heroku.RequestKey.AppName));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw noBody();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public List<StackInfo> getResponse(byte[] bytes, int status) {
        if (Http.Status.OK.equals(status)) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to list stacks.", status, bytes);
        }
    }
}
