package com.heroku.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.heroku.api.connection.Connection;

import java.io.IOException;


/**
 * Guice Module for providing tests with authentication information.
 *
 * @author Naaman Newbold
 */
public abstract class ConnectionTestModule<F extends Connection> extends AbstractModule {

    @Override
    protected void configure() {
        // not implemented
    }

    abstract F getConnectionImpl() throws IOException;

    @Provides
    public Connection<?> getConnection() throws IOException {
        return (Connection<?>) getConnectionImpl();
    }


}
