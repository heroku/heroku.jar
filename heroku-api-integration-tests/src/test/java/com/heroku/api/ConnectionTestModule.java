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
        if (Heroku.Config.ENDPOINT.isDefault() && !IntegrationTestConfig.TEST_AGAINST_PRODUCTION.getRequiredConfig().equals("true")) {
            throw new IllegalStateException("The API endpoint is set to production and TEST_AGAINST_PRODUCTION is not set to true");
        }
    }

    abstract F getConnectionImpl() throws IOException;

    @Provides
    public Connection<?> getConnection() throws IOException {
        return (Connection<?>) getConnectionImpl();
    }


}
