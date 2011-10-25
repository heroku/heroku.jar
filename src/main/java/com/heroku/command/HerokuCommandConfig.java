package com.heroku.command;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuCommandConfig<T extends Enum> {
    private final Map<T, String> config = new HashMap<T, String>();

    public void set(T key, String value) {
        config.put(key, value);
    }

    public String get(T key) {
        return config.get(key);
    }
}
