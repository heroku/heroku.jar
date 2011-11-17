package com.heroku.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.heroku.api.connection.Connection;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.*;

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


    @Provides
    AuthenticationTestCredentials getCredentials() throws IOException {
        return new AuthenticationTestCredentials(ENDPOINT.getRequiredConfig(), USER.getRequiredConfig(), PASSWORD.getRequiredConfig());
    }

    public static class AuthenticationTestCredentials {
        public final String endpoint;
        public final String username;
        public final String password;

        public AuthenticationTestCredentials(String endpoint, String username, String password) {
            this.endpoint = endpoint;
            this.username = username;
            this.password = password;
        }
    }
}
