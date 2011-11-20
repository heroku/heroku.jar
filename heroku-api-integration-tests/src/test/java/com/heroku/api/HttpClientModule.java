package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.exception.RequestFailedException;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.APIKEY;

public class HttpClientModule extends ConnectionTestModule<HttpClientConnection> {
    @Provides()
    HttpClientConnection getConnectionImpl() throws IOException {
        try {
            // skip login -- login creates additional, unnecessary http overhead. this uses
            // the api key directly to avoid login.
            return new HttpClientConnection(APIKEY.getRequiredConfig());
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
