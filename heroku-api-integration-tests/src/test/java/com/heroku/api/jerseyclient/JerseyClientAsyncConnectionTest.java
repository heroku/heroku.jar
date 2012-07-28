package com.heroku.api.jerseyclient;

import com.google.inject.Inject;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.JerseyClientModule;
import com.heroku.api.User;
import com.heroku.api.connection.JerseyClientAsyncConnection;
import com.heroku.api.request.user.UserInfo;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

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
