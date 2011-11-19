package com.heroku.api.json;


import java.util.Iterator;
import java.util.ServiceLoader;

public class Json {

    private static volatile boolean initialized = false;
    private static JsonParser parser;
    private static Object lock = new Object();

    public static JsonParser getJsonParser() {
        if (parser != null) return parser;
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
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

        }
        return parser;
    }


}
