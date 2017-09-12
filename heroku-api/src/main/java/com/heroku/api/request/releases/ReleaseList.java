package com.heroku.api.request.releases;

import com.heroku.api.Heroku;
import com.heroku.api.Release;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.util.Range;

import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ReleaseList implements Request<Range<Release>> {

    private Map<String,String> headers = new HashMap<>();

    private final RequestConfig config;

    public ReleaseList(String appName) {
        this.config = new RequestConfig().app(appName);
    }

    public ReleaseList(String appName, String range) {
        this(appName);
        this.headers.put("Range", range);
    }
    
    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Releases.format(config.getAppName());
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
    public Map<String, Object> getBodyAsMap() {
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
    public Range<Release> getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else if (status == 206) {
            Range<Release> r = Json.parse(bytes, ReleaseList.class);
            r.setNextRange(responseHeaders.get("Next-Range"));
            return r;
        }
        throw new RequestFailedException("Unable to list releases.", status, bytes);
    }
}
