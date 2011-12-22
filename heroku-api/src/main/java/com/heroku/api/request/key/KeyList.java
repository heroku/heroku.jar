package com.heroku.api.request.key;

import com.heroku.api.Heroku;
import com.heroku.api.Key;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.XmlParser;
import com.heroku.api.request.Request;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
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
public class KeyList implements Request<List<Key>> {

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
    public List<Key> getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            if (new String(bytes).contains("nil-classes")) {
                return new ArrayList<Key>();
            }
            Keys keys = new XmlParser().parse(bytes, Keys.class);
            return keys.getKey();
        } else {
            throw new RequestFailedException("Unable to list keys.", status, bytes);
        }
    }

    @XmlRootElement
    static class Keys {
        List<Key> keys;

        public List<Key> getKey() {
            return keys;
        }

        public void setKey(List<Key> keylist) {
            this.keys = keylist;
        }
    }
}
