package com.heroku.api;

import com.google.inject.Provides;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.parser.JsonSelector;
import com.heroku.api.parser.Parser;
import com.heroku.api.request.login.BasicAuthLogin;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.CONFIG;

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
