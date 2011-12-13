package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.parser.TypeReference;
import com.heroku.api.request.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppList implements Request<List<App>> {

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Apps.value;
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
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public List<App> getResponse(byte[] in, int code) {
        if (code == 200)
            return Json.getJsonParser().parse(in, new TypeReference<List<App>>(){}.getType());
        else
            throw new RequestFailedException("AppList Failed", code, in);
    }
}