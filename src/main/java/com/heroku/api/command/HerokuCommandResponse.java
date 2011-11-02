package com.heroku.api.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface HerokuCommandResponse {
    boolean isSuccess();
    Object get(String key);
}
