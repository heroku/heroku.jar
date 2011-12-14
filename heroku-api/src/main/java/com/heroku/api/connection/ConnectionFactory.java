package com.heroku.api.connection;

import java.util.ServiceLoader;

/**
 * Service locator that determines the default Connection implementation a user is
 * using.
 *
 * @author Naaman Newbold
 */
public class ConnectionFactory {

    static final ServiceLoader<ConnectionProvider> loader = ServiceLoader.load(ConnectionProvider.class);

    public static Connection get(String username, String password) {
        for (ConnectionProvider cp : loader) {
            Connection conn = cp.get(username, password);
            if (conn != null)
                return conn;
        }
        throw new IllegalArgumentException("ConnectionProvider not found for " + username);
    }

    public static Connection get(String apiKey) {
        for (ConnectionProvider cp : loader) {
            Connection conn = cp.get(apiKey);
            if (conn != null)
                return conn;
        }
        throw new IllegalArgumentException("ConnectionProvider not found for " + apiKey);
    }

}
