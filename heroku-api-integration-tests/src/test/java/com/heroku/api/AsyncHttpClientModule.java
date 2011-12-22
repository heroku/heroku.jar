package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.request.login.BasicAuthLogin;
import com.heroku.api.connection.AsyncHttpClientConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

public class AsyncHttpClientModule extends ConnectionTestModule {
    @Provides()
    AsyncHttpClientConnection getConnectionImpl() throws IOException {
        try {
            IntegrationTestConfig.TestUser testUser = IntegrationTestConfig.CONFIG.getDefaultUser();
            return new AsyncHttpClientConnection(new BasicAuthLogin(testUser.getUsername(), testUser.getPassword()));
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
