package com.heroku.api.command;

import com.heroku.api.Heroku;

import java.util.EnumMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CommandConfig {
    private final Map<Heroku.RequestKey, String> config = new EnumMap<Heroku.RequestKey, String>(Heroku.RequestKey.class);

    public CommandConfig onStack(Heroku.Stack stack) {
        return with(Heroku.RequestKey.stack, stack.value);
    }

    public CommandConfig app(String appName) {
        return with(Heroku.RequestKey.appName, appName);
    }

    public CommandConfig with(Heroku.RequestKey key, String value) {
        CommandConfig newConfig = new CommandConfig();
        newConfig.config.putAll(config);
        newConfig.config.put(key, value);
        return newConfig;
    }

    public String get(Heroku.RequestKey key) {
        return config.get(key);
    }

}
