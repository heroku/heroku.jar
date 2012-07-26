package com.heroku.api.jerseyclient;

import com.google.inject.Inject;
import com.heroku.api.*;
import com.heroku.api.connection.AsyncConnection;
import com.heroku.api.connection.AsyncHttpClientConnection;
import com.heroku.api.connection.JerseyClientAsyncConnection;
import com.heroku.api.request.app.AppList;
import com.heroku.api.request.user.UserInfo;
import com.twitter.util.Duration;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testng.Assert.assertEquals;

/**
 * @author Ryan Brainard
 */
@Guice(modules = JerseyClientModule.class)
public class JerseyClientAsyncConnectionTest {

    @Inject
    JerseyClientAsyncConnection connection;

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Test
    public void asyncTests() throws ExecutionException, TimeoutException, InterruptedException {
        Future<User> jsonArrayResponseFuture = connection.executeAsync(new UserInfo(), apiKey);
        User user = (User) jsonArrayResponseFuture.get(10, TimeUnit.SECONDS);
        assertEquals(user.getEmail(), IntegrationTestConfig.CONFIG.getDefaultUser().getUsername());
    }

}
