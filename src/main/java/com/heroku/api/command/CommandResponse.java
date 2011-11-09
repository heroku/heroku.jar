package com.heroku.api.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface CommandResponse {
    boolean isSuccess();
    Object get(String key);
    byte[] getRawData();
}
