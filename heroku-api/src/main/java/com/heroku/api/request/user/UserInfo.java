package com.heroku.api.request.user;

import com.heroku.api.Heroku;
import com.heroku.api.User;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class UserInfo implements Request<User> {
    
    private final RequestConfig config;
    
    public UserInfo() {
        config = new RequestConfig();
    }
    
    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.User.value;
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
    public User getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to get user info.", status, bytes);
        }
    }
}
