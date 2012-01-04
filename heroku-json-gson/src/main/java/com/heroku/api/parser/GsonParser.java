package com.heroku.api.parser;


import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class GsonParser implements Parser {

    @Override
    public <T> T parse(byte[] data, final Type type) {
		try {
        	return new Gson().<T>fromJson(getReader(data), type);
		} catch(Exception e) {
			throw new RuntimeException("Error parsing result "+new String(data),e);
		}
    }

    private Reader getReader(byte[] raw) {
        return new InputStreamReader(new ByteArrayInputStream(raw));
    }
}
