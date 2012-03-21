package com.heroku.api.parser;

import com.heroku.api.exception.ParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JacksonParser implements Parser {
    @Override
    public <T> T parse(byte[] data, Type type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = mapper.constructType(type);
        try {
            return mapper.readValue(data, javaType);
        } catch (IOException e) {
            throw new ParseException("Unable to parse data.", e);
        }
    }

}
