package com.heroku.api.request.key;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.response.Unit;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * Deletes all SSH keys.
 *
 * @author Naaman Newbold
 */
public class KeysRemoveAll implements Request<Unit> {

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Keys.format();
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
    public Unit getResponse(byte[] bytes, int status) {
        if (Http.Status.OK.equals(status)) {
            return Unit.unit;
        } else {
            throw new RequestFailedException("Unable to delete all keys.", status, bytes);
        }
    }
}
