package com.heroku.api.request.sharing;

import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.request.response.XmlArrayResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import static com.heroku.api.Heroku.RequestKey.*;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CollabList implements Request<XmlArrayResponse> {

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
        return Heroku.Resource.Collaborators.format(config.get(AppName));
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
        return Http.Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public XmlArrayResponse getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return new XmlArrayResponse(bytes);
        } else {
            throw new RequestFailedException("List collaborators failed.", status, bytes);
        }
    }
}
