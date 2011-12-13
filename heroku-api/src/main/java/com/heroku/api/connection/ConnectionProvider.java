package com.heroku.api.connection;

import com.heroku.api.HerokuAPIConfig;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface ConnectionProvider<F> {

    Connection<F> get(HerokuAPIConfig config);

}
