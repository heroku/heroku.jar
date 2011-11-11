package com.heroku.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.heroku.api.command.BasicAuthLoginCommand;
import com.heroku.api.connection.Connection;
import com.heroku.api.connection.HttpClientConnection;

import java.io.IOException;
import java.util.Properties;

/**
 * Guice Module for providing tests with authentication information.
 *
 * @author Naaman Newbold
 */
public class ConnectionTestModule extends AbstractModule {

    @Override
    protected void configure() {
        // not implemented
    }

    @Provides
    Connection getConnection() throws IOException {
        AuthenticationTestCredentials cred = getCredentials();
        return new HttpClientConnection(new BasicAuthLoginCommand(cred.username, cred.password));
    }

    @Provides
    Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(ConnectionTestModule.class.getResourceAsStream("/auth-test.properties"));
        return properties;
    }

    @Provides
    AuthenticationTestCredentials getCredentials() throws IOException {
        Properties props = getProperties();
        return new AuthenticationTestCredentials(
                props.getProperty("endpoint")
                , props.getProperty("username")
                , props.getProperty("password")
        );
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
