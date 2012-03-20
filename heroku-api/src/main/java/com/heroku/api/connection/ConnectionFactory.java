package com.heroku.api.connection;

import java.util.ServiceLoader;

/**
 * Service locator that determines the default Connection implementation a user is
 * using.
 *
 * @author Naaman Newbold
 */
public class ConnectionFactory {

    static final ServiceLoader<ConnectionProvider> loader = ServiceLoader.load(ConnectionProvider.class, ConnectionFactory.class.getClassLoader());

    public static Connection get() {
        for (ConnectionProvider cp : loader) {
            Connection conn = cp.getConnection();
            if (conn != null)
                return conn;
        }
        throw new IllegalArgumentException("ConnectionProvider not found");
    }

}
