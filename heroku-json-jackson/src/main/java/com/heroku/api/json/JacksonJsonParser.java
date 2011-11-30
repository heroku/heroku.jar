package com.heroku.api.json;

import com.heroku.api.exception.ParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.*;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JacksonJsonParser implements JsonParser {
    @Override
    public <T> T parse(byte[] data, Type type) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.constructType(type);
        try {
            return mapper.readValue(data, javaType);
        } catch (IOException e) {
            throw new ParseException("Unable to parse data.", e);
        }
    }

    @Override
    public Map<String, String> parseMap(byte[] raw) {
        Type type = new TypeReference<Map<String, String>>() {
        }.getType();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, mapper.constructType(type));
        } catch (IOException e) {
            throw new ParseException("Unable to parse data.", e);
        }
    }

    @Override
    public List<Map<String, String>> parseArray(byte[] raw) {
        Type type = new TypeReference<List<Map<String, String>>>() {
        }.getType();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, mapper.constructType(type));
        } catch (IOException e) {
            throw new ParseException("Unable to parse data.", e);
        }
    }
}
