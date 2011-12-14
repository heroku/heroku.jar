package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.request.login.BasicAuthLogin;
import com.heroku.api.connection.FinagleConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.PASSWORD;
import static com.heroku.api.IntegrationTestConfig.USER;

public class FinagleModule extends ConnectionTestModule {

    @Provides
    FinagleConnection getConnectionImpl() throws IOException {

        try {
            return FinagleConnection.apply(new BasicAuthLogin(USER.getRequiredConfig(), PASSWORD.getRequiredConfig()));
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
