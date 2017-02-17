package com.heroku.api.request.sharing;

import com.heroku.api.Collaborator;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CollabList implements Request<List<Collaborator>> {

    private final RequestConfig config;

    public CollabList(String appName) {
        config = new RequestConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Collaborators.format(config.getAppName());
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
        return Collections.emptyMap();
    }

    @Override
    public List<Collaborator> getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("List collaborators failed.", status, bytes);
        }
    }

}
