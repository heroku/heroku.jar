package com.heroku.api.request.addon;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.response.JsonArrayResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonList implements Request<JsonArrayResponse> {

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
    public JsonArrayResponse getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return new JsonArrayResponse(bytes);
        } else {
            throw new RequestFailedException("Unable to list addons.", status, bytes);
        }
    }
}
