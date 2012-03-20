package com.heroku.api.finagle;

import com.google.inject.Inject;
import com.heroku.api.FinagleModule;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.Key;
import com.heroku.api.connection.FinagleConnection;
import com.heroku.api.request.key.KeyList;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertNotNull;


@Guice(modules = FinagleModule.class)
public class FinagleConnectionTest {

    @Inject
    FinagleConnection connection;

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Test
    @SuppressWarnings("unchecked")
    public void asyncTests() {
        Future<List<Key>> jsonArrayResponseFuture = connection.executeAsync(new KeyList(), apiKey);
        List<Key> keys = (List<Key>) jsonArrayResponseFuture.get(Duration.fromTimeUnit(10L, TimeUnit.SECONDS)).get();
        assertNotNull(keys);
    }

}
