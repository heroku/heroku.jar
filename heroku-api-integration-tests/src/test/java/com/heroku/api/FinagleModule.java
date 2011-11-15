package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.command.login.BasicAuthLogin;
import com.heroku.api.connection.Connection;
import com.heroku.api.connection.FinagleConnection;

import java.io.IOException;


public class FinagleModule extends ConnectionTestModule {

    @Provides
    Connection<?> getConnection() throws IOException {
        AuthenticationTestCredentials cred = getCredentials();
        return new FinagleConnection(new BasicAuthLogin(cred.username, cred.password, cred.endpoint));
    }
}
