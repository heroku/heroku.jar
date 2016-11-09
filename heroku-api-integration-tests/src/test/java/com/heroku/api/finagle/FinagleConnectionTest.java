package com.heroku.api.finagle;

import com.google.inject.Inject;
import com.heroku.api.FinagleModule;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.User;
import com.heroku.api.connection.FinagleConnection;
import com.heroku.api.request.user.UserInfo;
import com.twitter.util.Await;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;


@Guice(modules = FinagleModule.class)
public class FinagleConnectionTest {

    @Inject
    FinagleConnection connection;

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Test(timeOut = 10000)
    @SuppressWarnings("unchecked")
    public void asyncTests() {
        Future<User> jsonArrayResponseFuture = connection.executeAsync(new UserInfo(), apiKey);
        User user = null;
        try {
            user = Await.result(jsonArrayResponseFuture, Duration.fromTimeUnit(10L, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getEmail(), IntegrationTestConfig.CONFIG.getDefaultUser().getUsername());
    }

}
