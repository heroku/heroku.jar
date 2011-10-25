package com.heroku.connection;

import java.io.IOException;

/**
 * Storage for Heroku credentials and the means for connecting to the API.
 *
 * @author Naaman Newbold
 */
public interface HerokuConnectionProvider {
    static final String DEFAULT_ENDPOINT = "https://api.heroku.com";
    HerokuConnection getConnection() throws IOException, HerokuAPIException;
}
