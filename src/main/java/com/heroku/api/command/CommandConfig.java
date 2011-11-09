package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuStack;

import java.util.EnumMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CommandConfig {
    private final Map<HerokuRequestKey, String> config = new EnumMap<HerokuRequestKey, String>(HerokuRequestKey.class);

    public CommandConfig onStack(HerokuStack stack) {
        return with(HerokuRequestKey.stack, stack.value);
    }

    public CommandConfig app(String appName) {
        return with(HerokuRequestKey.name, appName);
    }

    public CommandConfig with(HerokuRequestKey key, String value) {
        CommandConfig newConfig = new CommandConfig();
        newConfig.config.putAll(config);
        newConfig.config.put(key, value);
        return newConfig;
    }

    public String get(HerokuRequestKey key) {
        return config.get(key);
    }

}
