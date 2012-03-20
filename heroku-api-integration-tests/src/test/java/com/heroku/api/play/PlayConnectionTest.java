package com.heroku.api.play;

import com.google.inject.Inject;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.Key;
import com.heroku.api.PlayModule;
import com.heroku.api.connection.PlayWSConnection;
import com.heroku.api.request.key.KeyList;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import play.api.libs.concurrent.Promise;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertNotNull;

@Guice(modules = PlayModule.class)
public class PlayConnectionTest {

    @Inject
    PlayWSConnection connection;

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();


    @Test
    @SuppressWarnings("unchecked")
    public void asyncTests() {
        Promise<List<Key>> jsonArrayResponseFuture = connection.executeAsync(new KeyList(), apiKey);
        List<Key> keys = jsonArrayResponseFuture.await(10L, TimeUnit.SECONDS).get();
        assertNotNull(keys);
    }

}

