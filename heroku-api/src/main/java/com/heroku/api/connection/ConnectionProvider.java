package com.heroku.api.connection;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface ConnectionProvider {
    
    Connection get(String username, String password);

    Connection get(String apiKey);

}
