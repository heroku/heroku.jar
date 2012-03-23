package com.heroku.api.connection;

/**
 * Determines which implementation of {@link Connection} to use. Uses a {@link java.util.ServiceLoader} to find
 * the implementation. These are located in implementation modules' resource folders.
 *
 * @author Naaman Newbold
 */
public interface ConnectionProvider {

    Connection getConnection();

}
