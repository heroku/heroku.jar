package com.heroku;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuBasicAuthConnectionProvider;
import com.heroku.connection.HerokuConnection;
import com.heroku.connection.HerokuConnectionProvider;

import java.io.IOException;
import java.util.Properties;

/**
 * Guice Module for providing tests with authentication information.
 * @author Naaman Newbold
 */
public class ConnectionTestModule extends AbstractModule {

    @Override
    protected void configure() {
        // not implemented
    }

    @Provides
    HerokuConnection getConnection() throws IOException, HerokuAPIException {
        AuthenticationTestCredentials cred = getCredentials();
        return new HerokuBasicAuthConnectionProvider(cred.username, cred.password).getConnection();
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
