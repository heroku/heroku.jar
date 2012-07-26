package parser;

import com.heroku.api.exception.ParseException;
import com.heroku.api.parser.Json;
import com.heroku.api.parser.Parser;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.impl.provider.entity.ByteArrayProvider;
import com.sun.jersey.core.provider.CompletableReader;
import com.sun.jersey.json.impl.provider.entity.JacksonProviderProxy;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
