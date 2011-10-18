package com.heroku.connection;

import java.io.IOException;

/**
 * Storage for Heroku credentials and the means for connecting to the API.
 *
 * @author Naaman Newbold
 */
public abstract class HerokuConnectionConfig {
    /* package */ static final String DEFAULT_ENDPOINT = "https://api.heroku.com";

    public static HerokuConnectionConfig newInstance(String username, String password) {
        return newInstance(username, password, DEFAULT_ENDPOINT);
    }

    public static HerokuConnectionConfig newInstance(String username, String password, String endpoint) {
        return new HerokuConnectionLoginConfig(username, password, endpoint);
    }

    public abstract HerokuConnection connect() throws IOException;
}
