package com.heroku.api.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JsonArrayResponse implements CommandResponse {

    private final List<Map<String, String>> data;
    private final boolean success;
    private final byte[] rawData;

    public JsonArrayResponse(byte[] data, boolean success) {
        this.rawData = data;
        Type listType = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();
        this.data = Collections.unmodifiableList(new Gson().<List<Map<String, String>>>fromJson(new String(data), listType));
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
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
