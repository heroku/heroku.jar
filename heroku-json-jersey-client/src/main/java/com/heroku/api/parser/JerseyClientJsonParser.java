package com.heroku.api.parser;

import com.heroku.api.exception.ParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Ryan Brainard
 */
public class JerseyClientJsonParser implements Parser {
    @Override
    public <T> T parse(byte[] data, Type type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = TypeFactory.type(type);
        try {
            return mapper.readValue(data, 0, data.length, javaType);
        } catch (IOException e) {
            throw new ParseException("Unable to parse data.", e);
        }
    }
}
