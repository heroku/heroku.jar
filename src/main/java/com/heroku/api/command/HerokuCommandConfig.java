package com.heroku.api.command;

import com.heroku.api.HerokuStack;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuCommandConfig {
    private final Map<HerokuRequestKey, String> config = new HashMap<HerokuRequestKey, String>();

    public HerokuCommandConfig onStack(HerokuStack stack) {
        set(HerokuRequestKey.stack, stack.value);
        return this;
    }

    public HerokuCommandConfig app(String appName) {
        set(HerokuRequestKey.name, appName);
        return this;
    }

    public HerokuCommandConfig set(HerokuRequestKey key, String value) {
        config.put(key, value);
        return this;
    }

    public String get(HerokuRequestKey key) {
        return config.get(key);
    }
}
