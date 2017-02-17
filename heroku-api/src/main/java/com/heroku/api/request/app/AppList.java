package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.util.Range;

import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppList implements Request<Range<App>> {

    private Map<String,String> headers = new HashMap<>();

    public AppList() {

    }

    public AppList(String range) {
        this();
        this.headers.put("Range", range);
    }

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
    public Map<String,Object> getBodyAsMap() {
        throw noBody();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Range<App> getResponse(byte[] in, int code, Map<String, String> responseHeaders) {
        if (code == 200) {
            return Json.parse(in, AppList.class);
        } else if (code == 206) {
            Range<App> r = Json.parse(in, AppList.class);
            r.setNextRange(responseHeaders.get("Next-Range"));
            return r;
        } else {
            throw new RequestFailedException("AppList Failed", code, in);
        }
    }
}