package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.command.login.BasicAuthLogin;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;


public class HttpClientModule extends ConnectionTestModule<HttpClientConnection> {
    @Provides()
    HttpClientConnection getConnectionImpl() throws IOException {
        AuthenticationTestCredentials cred = getCredentials();
        try {
            return new HttpClientConnection(new BasicAuthLogin(cred.username, cred.password, cred.endpoint));
        } catch (RequestFailedException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBody());
            System.out.flush();
            e.printStackTrace();
            throw e;
        }
    }
}
