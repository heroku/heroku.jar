package com.heroku.api.connection;

import com.heroku.api.HerokuAPIConfig;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface ConnectionProvider {

    Connection get(HerokuAPIConfig config);

}
