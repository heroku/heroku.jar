package com.heroku.api.request.key;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.response.XmlArrayResponse;

import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class KeyList implements Request<XmlArrayResponse> {

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Keys.value;
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
        return Http.Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public XmlArrayResponse getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode)
            return new XmlArrayResponse(bytes);
        else
            throw new RequestFailedException("Unable to list keys.", status, bytes);
    }
}
