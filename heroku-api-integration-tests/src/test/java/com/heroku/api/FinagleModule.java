package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.FinagleConnection;
import com.heroku.api.connection.TwitterFutureConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

public class FinagleModule extends ConnectionTestModule {

    @Provides
    TwitterFutureConnection getConnectionImpl() throws IOException {

        try {
            return FinagleConnection.apply();
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
