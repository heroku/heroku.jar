package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.request.login.BasicAuthLogin;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.CONFIG;

public class HttpClientModule extends ConnectionTestModule {
    @Provides()
    HttpClientConnection getConnectionImpl() throws IOException {
        try {
            IntegrationTestConfig.TestUser testUser = CONFIG.getDefaultUser();
            return new HttpClientConnection(new BasicAuthLogin(testUser.getUsername(), testUser.getPassword()));
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
