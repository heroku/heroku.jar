package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.AsyncConnection;
import com.heroku.api.connection.AsyncHttpClientConnection;
import com.heroku.api.connection.ListenableFutureConnection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.parser.JsonSelector;
import com.heroku.api.parser.Parser;
import connection.JerseyClientAsyncConnection;
import parser.JerseyClientJsonParser;

import java.io.IOException;
import java.util.concurrent.Future;

public class JerseyClientModule extends ConnectionTestModule {

    Parser parser;

    public JerseyClientModule() {

    }

    public JerseyClientModule(Parser p) {
        this.parser = p;
    }

    @Override
    protected void configure() {
        if (parser != null) JsonSelector.selectParser(parser);
    }

    @Provides()
    AsyncConnection<Future<?>> getConnectionImpl() throws IOException {
        try {
            return new JerseyClientAsyncConnection();
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
