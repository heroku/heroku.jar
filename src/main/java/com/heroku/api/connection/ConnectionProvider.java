package com.heroku.api.connection;

import java.io.IOException;

/**
 * Storage for Heroku credentials and the means for connecting to the API.
 *
 * @author Naaman Newbold
 */
public interface ConnectionProvider {
    static final String DEFAULT_ENDPOINT = "https://api.heroku.com";
    Connection getConnection() throws IOException;
}
