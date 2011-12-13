package com.heroku.api.connection;

import com.heroku.api.HerokuAPIConfig;

import java.util.ServiceLoader;

/**
 * Service locator that determines the default Connection implementation a user is
 * using.
 *
 * @author Naaman Newbold
 */
public class ConnectionFactory {

    public static Connection<?> get(HerokuAPIConfig config) {
        ServiceLoader<ConnectionProvider> loader = ServiceLoader.load(ConnectionProvider.class);
        for (ConnectionProvider conn : loader) {
            Connection newConnection = conn.get(config);
            if (newConnection != null) {
                return newConnection;
            }
        }
        throw new IllegalArgumentException("No connection providers found for the provided config." + config.toString());
    }

}
