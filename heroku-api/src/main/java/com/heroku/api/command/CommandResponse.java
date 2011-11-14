package com.heroku.api.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface CommandResponse {

    Object get(String key);

    byte[] getRawData();

    Object getData();
}
