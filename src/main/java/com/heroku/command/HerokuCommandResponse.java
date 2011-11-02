package com.heroku.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface HerokuCommandResponse {
    boolean isSuccess();
    Object get(String key);
}
