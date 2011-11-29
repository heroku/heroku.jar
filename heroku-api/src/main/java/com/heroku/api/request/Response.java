package com.heroku.api.request;

import com.heroku.api.Heroku;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface Response {

    Object get(String key);
    
    Object get(Heroku.ResponseKey key);

    byte[] getRawData();

    Object getData();
}
