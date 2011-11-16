package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.command.login.BasicAuthLogin;
import com.heroku.api.connection.HttpClientConnection;

import java.io.IOException;


public class HttpClientModule extends ConnectionTestModule<HttpClientConnection> {
    @Provides()
    HttpClientConnection getConnectionImpl() throws IOException {
        AuthenticationTestCredentials cred = getCredentials();
        return new HttpClientConnection(new BasicAuthLogin(cred.username, cred.password, cred.endpoint));
    }
}
