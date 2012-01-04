package com.heroku.api.parser;


import com.heroku.api.exception.ParseException;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Json {

    static class Holder {
        static Parser parser;

        static {
            ServiceLoader<Parser> loader = ServiceLoader.load(Parser.class, Parser.class.getClassLoader());
            Iterator<Parser> iterator = loader.iterator();
            if (iterator.hasNext()) {
                parser = iterator.next();
            } else {
                throw new IllegalStateException("Unable to load a JSONProvider, please make sure you have a com.heroku.api.json.JSONParser implementation" +
                        "on your classpath that can be discovered and loaded via java.util.ServiceLoader");
            }
        }
    }



    /**
     * Calls Parser.parse() using the generic type T for Request<T> given Request<T> is the interface for the
     * classType parameter. If it can't find an appropriate type, it errors out with a ParseException.
     * <p/>
     * The following code sample illustrates typical usage in the context of a request to Heroku's API.
     * The byte array is provided from a connection.execute(request) call, which is a JSON response from
     * the server. getClass() provides the classType, which in this case extends Request<T>. The return
     * value from the parse method will be App.
     * <code>
     * public class SampleRequest implements Request<App> {
     * ...
     * public App getResponse(byte[] data, int status) {
     * return Json.parse(data, getClass());
     * }
     * }
     * </code>
     *
     * @param data      JSON byte array to be parsed
     * @param classType The Request implementation class type. This is typically given the calling class as
     *                  an argument.
     * @return T
     */
    public static <T> T parse(byte[] data, Class<? extends Request<T>> classType) {
        // get all interfaces for the class
        Type[] genericInterfaces = classType.getGenericInterfaces();
        for (Type interfaceType : genericInterfaces) {
            // ensure the Type is a generic before doing a conversion
            if (interfaceType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
                // make sure the parameterized type is a Request<T>
                if (Request.class.equals(parameterizedType.getRawType())) {
                    // get the first type since we only have one generic defined for Request<T>
                    Type type = parameterizedType.getActualTypeArguments()[0];
                    try {
                        return Holder.parser.parse(data, type);
                    } catch (RuntimeException e) {
                        String json = HttpUtil.getUTF8String(data);
                        throw new RuntimeException("Failed to parse JSON:" + json, e);
                    }
                }
            }
        }
        throw new ParseException("Request<T> was not found for " + classType.toString());
    }

}
