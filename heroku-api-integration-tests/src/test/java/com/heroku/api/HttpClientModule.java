package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.FutureConnection;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.parser.JsonSelector;
import com.heroku.api.parser.Parser;

import java.io.IOException;

public class HttpClientModule extends ConnectionTestModule {

    Parser parser;

    public HttpClientModule() {

    }

    public HttpClientModule(Parser p) {
        this.parser = p;
    }

    @Override
    protected void configure() {
        if (parser != null) JsonSelector.selectParser(parser);
    }

    @Provides()
    FutureConnection getConnectionImpl() throws IOException {
        try {
            return new HttpClientConnection();
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
