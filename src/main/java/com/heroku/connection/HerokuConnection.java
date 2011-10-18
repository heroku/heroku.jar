package com.heroku.connection;

/**
 * Connection manager for connecting to the Heroku API.
 *
 * @author Naaman Newbold
 */
public interface HerokuConnection {
    public String getApiKey();
    public String getId();
    public String getEmail();
}
