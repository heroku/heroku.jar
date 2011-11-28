package com.heroku.api.request;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface Response {

    Object get(String key);

    byte[] getRawData();

    Object getData();
}
