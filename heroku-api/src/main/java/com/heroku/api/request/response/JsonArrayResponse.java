package com.heroku.api.request.response;

import com.heroku.api.Heroku;
import com.heroku.api.request.Response;
import com.heroku.api.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JsonArrayResponse implements Response {

    private final List<Map<String, String>> data;
    private final byte[] rawData;

    public JsonArrayResponse(byte[] bytes) {
        this.rawData = bytes;
        this.data = Json.getJsonParser().parseArray(this.rawData);
    }

    @Override
    public Object get(String key) {
        for (Map<String, String> obj : data) {
            if (obj.containsKey("id") && obj.get("id").equals(key)) {
                return obj;
            } else if (obj.containsKey("name") && obj.get("name").equals(key)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public Object get(Heroku.ResponseKey key) {
        return get(key.toString());
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }

    @Override
    public List<Map<String, String>> getData() {
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        for (Map<String, String> map : data) {
            ret.add(new HashMap<String, String>(map));
        }
        return ret;
    }
}
