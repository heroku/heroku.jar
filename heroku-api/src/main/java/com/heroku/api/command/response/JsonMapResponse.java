package com.heroku.api.command.response;

import com.heroku.api.command.CommandResponse;
import com.heroku.api.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JsonMapResponse implements CommandResponse {

    private final Map<String, String> data;
    private final byte[] rawData;

    public JsonMapResponse(byte[] bytes) {
        this.rawData = bytes;
        this.data = Json.getJsonParser().parseMap(this.rawData);
    }


    @Override
    public String get(String key) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException(key + " is not present.");
        }
        return data.get(key);
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }

    @Override
    public Map<String, String> getData() {
        return new HashMap<String, String>(data);
    }

    @Override
    public String toString() {
        String stringValue = "{ ";
        String separator = "";
        for (Map.Entry<String, String> e : data.entrySet()) {
            stringValue += separator + e.getKey() + " : " + e.getValue();
            separator = ", ";
        }
        return stringValue + " }";
    }
}
