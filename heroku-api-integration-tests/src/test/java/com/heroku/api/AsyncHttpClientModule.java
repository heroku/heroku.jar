package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.AsyncHttpClientConnection;
import com.heroku.api.connection.ListenableFutureConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

public class AsyncHttpClientModule extends ConnectionTestModule {
    @Provides()
    ListenableFutureConnection getConnectionImpl() throws IOException {
        try {
            return new AsyncHttpClientConnection();
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
