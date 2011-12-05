package com.heroku.api.request.releases;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.model.Release;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ListReleases implements Request<List<Release>> {

    private final RequestConfig config;

    public ListReleases(String appName) {
        this.config = new RequestConfig().app(appName);
    }
    
    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Releases.format(config.get(Heroku.RequestKey.AppName));
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
        return new HashMap<String, String>();
    }

    @Override
    public List<Release> getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        }
        throw new RequestFailedException("Unable to list releases.", status, bytes);
    }
}
