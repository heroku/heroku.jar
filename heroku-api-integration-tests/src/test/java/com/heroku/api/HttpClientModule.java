package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.command.BasicAuthLoginCommand;
import com.heroku.api.connection.Connection;
import com.heroku.api.connection.HttpClientConnection;

import java.io.IOException;


public class HttpClientModule extends ConnectionTestModule {
    @Provides
    Connection<?> getConnection() throws IOException {
        AuthenticationTestCredentials cred = getCredentials();
        return new HttpClientConnection(new BasicAuthLoginCommand(cred.username, cred.password, cred.endpoint));
    }
}
