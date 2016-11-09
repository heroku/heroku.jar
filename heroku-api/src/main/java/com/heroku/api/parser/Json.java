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
     * Proxy method for getting the Parser and calling encode().
     * @param object JSON byte array to be parsed.
     * @return The Object serialized to a JSON String
     */
    public static String encode(Object object) {
        return Holder.parser.encode(object);
    }

    /**
     * Proxy method for getting the Parser and calling parse().
     * @param data JSON byte array to be parsed.
     * @param type Deserialized type for the JSON data
     * @param <T> Deserialzed object type
     * @return The JSON data deserialized
     */
    public static <T> T parse(byte[] data, Type type) {
        return Holder.parser.parse(data, type);
    }

    /**
     * <p>
     * Calls Parser.parse() using the generic type T for Request given Request is the interface for the
     * classType parameter. If it can't find an appropriate type, it errors out with a ParseException.
     * </p>
     * <p>
     * The following code sample illustrates typical usage in the context of a request to Heroku's API.
     * The byte array is provided from a connection.execute(request) call, which is a JSON response from
     * the server. getClass() provides the classType, which in this case extends Request. The return
     * value from the parse method will be App.
     * </p>
     *
     * @param data      JSON byte array to be parsed
     * @param <T> Deserialzed object type
     * @param classType The Request implementation class type. This is typically given the calling class as
     *                  an argument.
     * @return object representing the data
     */
    public static <T> T parse(byte[] data, Class<? extends Request<T>> classType) {
        Type[] types = doResolveTypeArguments(classType, classType, Request.class);
        if (types == null || types.length == 0) {
            throw new ParseException("Request<T> was not found for " + classType);
        }
        Type type = types[0];
        try {
            return Holder.parser.parse(data, type);
        } catch (RuntimeException e) {
            String json = HttpUtil.getUTF8String(data);
            throw new ParseException("Failed to parse JSON:" + json, e);
        }
    }


    /*
     * slightly modded version of spring GenericTypeResolver methods
     */


    private static Type[] doResolveTypeArguments(Class ownerClass, Class classToIntrospect, Class genericIfc) {
        while (classToIntrospect != null) {
            if (genericIfc.isInterface()) {
                Type[] ifcs = classToIntrospect.getGenericInterfaces();
                for (Type ifc : ifcs) {
                    Type[] result = doResolveTypeArguments(ownerClass, ifc, genericIfc);
                    if (result != null) {
                        return result;
                    }
                }
            } else {
                Type[] result = doResolveTypeArguments(ownerClass, classToIntrospect.getGenericSuperclass(), genericIfc);
                if (result != null) {
                    return result;
                }
            }
            classToIntrospect = classToIntrospect.getSuperclass();
        }
        return null;
    }

    private static Type[] doResolveTypeArguments(Class ownerClass, Type ifc, Class genericIfc) {
        if (ifc instanceof ParameterizedType) {
            ParameterizedType paramIfc = (ParameterizedType) ifc;
            Type rawType = paramIfc.getRawType();
            if (genericIfc.equals(rawType)) {
                return paramIfc.getActualTypeArguments();

            } else if (genericIfc.isAssignableFrom((Class) rawType)) {
                return doResolveTypeArguments(ownerClass, (Class) rawType, genericIfc);
            }
        } else if (ifc != null && genericIfc.isAssignableFrom((Class) ifc)) {
            return doResolveTypeArguments(ownerClass, (Class) ifc, genericIfc);
        }
        return null;
    }


}
