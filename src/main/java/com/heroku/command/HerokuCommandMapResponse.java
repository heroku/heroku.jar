package com.heroku.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuCommandMapResponse implements HerokuCommandResponse {

    private final Map<String, String> data;
    private final boolean success;

    public HerokuCommandMapResponse(byte[] data, boolean success) {
        Type listType = new TypeToken<HashMap<String, String>>(){}.getType();
        this.data = Collections.unmodifiableMap(new Gson().<Map<String, String>>fromJson(new String(data), listType));

        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Object get(String key) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException(key + " is not present.");
        }
        return data.get(key);
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
