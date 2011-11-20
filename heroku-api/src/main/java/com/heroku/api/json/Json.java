package com.heroku.api.json;


import java.util.Iterator;
import java.util.ServiceLoader;

public class Json {

    private static class Holder {
        static JsonParser parser;

        static {
            ServiceLoader<JsonParser> loader = ServiceLoader.load(JsonParser.class);
            Iterator<JsonParser> iterator = loader.iterator();
            if (iterator.hasNext()) {
                parser = iterator.next();
            } else {
                throw new IllegalStateException("Unable to load a JSONProvider, please make sure you have a com.heroku.api.json.JSONParser implementation" +
                        "on your classpath that can be discovered and loaded via java.util.ServiceLoader");
            }
        }
    }

    public static JsonParser getJsonParser() {
        return Holder.parser;
    }
}
