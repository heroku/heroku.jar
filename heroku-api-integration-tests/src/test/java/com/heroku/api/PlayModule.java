package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.PlayWSConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.CONFIG;

public class PlayModule extends ConnectionTestModule {

    @Provides
    PlayWSConnection getConnectionImpl() throws IOException {

        try {
            IntegrationTestConfig.TestUser testUser = CONFIG.getDefaultUser();
            return PlayWSConnection.apply();
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
