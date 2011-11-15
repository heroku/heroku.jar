package com.heroku.api.command.app;

import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.response.JsonArrayResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppsCommand implements Command<JsonArrayResponse> {

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return HerokuResource.Apps.value;
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw HttpUtil.noBody();
    }

    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public JsonArrayResponse getResponse(InputStream in, int code) {
        if (code == 200)
            return new JsonArrayResponse(in);
        else
            throw new RequestFailedException("Apps Command Failed", code, in);
    }
}