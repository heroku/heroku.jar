package com.heroku.api.request;

import com.heroku.api.Heroku;

import java.util.EnumMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class RequestConfig {
    private final Map<Heroku.RequestKey, String> config = new EnumMap<Heroku.RequestKey, String>(Heroku.RequestKey.class);

    public RequestConfig onStack(Heroku.Stack stack) {
        return with(Heroku.RequestKey.Stack, stack.value);
    }

    public RequestConfig app(String appName) {
        return with(Heroku.RequestKey.AppName, appName);
    }

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

}
