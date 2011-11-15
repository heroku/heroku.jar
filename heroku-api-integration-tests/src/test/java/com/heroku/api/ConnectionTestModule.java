package com.heroku.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.heroku.api.connection.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Guice Module for providing tests with authentication information.
 *
 * @author Naaman Newbold
 */
public abstract class ConnectionTestModule extends AbstractModule {

    @Override
    protected void configure() {
        // not implemented
    }

    abstract Connection<?> getConnection() throws IOException;

    @Provides
    Properties getProperties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("auth-test.properties");
        properties.load(inputStream);
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
