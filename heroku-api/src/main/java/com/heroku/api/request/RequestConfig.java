package com.heroku.api.request;

import com.heroku.api.Heroku;

import java.util.EnumMap;
import java.util.Map;

/**
 * Wrapper class for an {@link EnumMap} to configure a {@link Request}. A {@link com.heroku.api.Heroku.RequestKey} serves as
 * a predetermined key for a name/value parameter.
 *
 * @author Naaman Newbold
 */
public class RequestConfig {
    private final Map<Heroku.RequestKey, String> config = new EnumMap<Heroku.RequestKey, String>(Heroku.RequestKey.class);

    /**
     * Sets the {@link Heroku.RequestKey.Stack} parameter.
     * @param stack A {@link com.heroku.api.Heroku.Stack} to specify in the config.
     * @return A new {@link RequestConfig}
     */
    public RequestConfig onStack(Heroku.Stack stack) {
        return with(Heroku.RequestKey.Stack, stack.value);
    }

    /**
     * Sets the {@link Heroku.RequestKey.AppName} parameter.
     * @param appName Name of the app to specify in the config.
     * @return A new {@link RequestConfig}
     */
    public RequestConfig app(String appName) {
        return with(Heroku.RequestKey.AppName, appName);
    }

    /**
     * Sets a {@link com.heroku.api.Heroku.RequestKey} parameter.
     * @param key
     * @param value
     * @return A new {@link RequestConfig}
     */
    public RequestConfig with(Heroku.RequestKey key, String value) {
        RequestConfig newConfig = new RequestConfig();
        newConfig.config.putAll(config);
        newConfig.config.put(key, value);
        return newConfig;
    }

    public String get(Heroku.RequestKey key) {
        return config.get(key);
    }
    
    public Map<Heroku.RequestKey, String> asMap() {
        return config;
    }

    public boolean has(Heroku.RequestKey key){
        return config.containsKey(key);
    }

}
