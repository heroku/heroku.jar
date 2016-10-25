package com.heroku.api.parser;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class GsonParser implements Parser {

    @Override
    public String encode(Object object) {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();
        return gson.toJson(object);
    }

    @Override
    public <T> T parse(byte[] data, final Type type) {
        Reader dataReader = getReader(data);
        JsonReader jsonReader = new JsonReader(dataReader);
        jsonReader.setLenient(true);
        return new Gson().<T>fromJson(jsonReader, type);
    }

    private Reader getReader(byte[] raw) {
        try {
            return new InputStreamReader(new ByteArrayInputStream(raw), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Somehow UTF-8 is unsupported", e);
        }
    }
}
