package com.heroku.api.json;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonJsonParser implements JsonParser {

    @Override
    public <T> T parse(byte[] data, final Type type) {
        return new Gson().<T>fromJson(getReader(data), type);
    }

    @Override
    public Map<String, String> parseMap(byte[] raw) {
        Type listType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return Collections.unmodifiableMap(new Gson().<Map<String, String>>fromJson(getReader(raw), listType));
    }

    @Override
    public List<Map<String, String>> parseArray(byte[] raw) {
        Type listType = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();
        return Collections.unmodifiableList(new Gson().<List<Map<String, String>>>fromJson(getReader(raw), listType));
    }

    private Reader getReader(byte[] raw) {
        return new InputStreamReader(new ByteArrayInputStream(raw));
    }
}
