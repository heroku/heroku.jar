package com.heroku.api.request.addon;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.model.Addon;
import com.heroku.api.request.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonList implements Request<List<Addon>> {

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Addons.value;
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
    public List<Addon> getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
        } else {
            throw new RequestFailedException("Unable to list addons.", status, bytes);
        }
    }
}
