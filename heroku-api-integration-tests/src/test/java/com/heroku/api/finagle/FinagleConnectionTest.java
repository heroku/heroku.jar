package com.heroku.api.finagle;

import com.google.inject.Inject;
import com.heroku.api.FinagleModule;
import com.heroku.api.connection.FinagleConnection;
import com.heroku.api.model.Key;
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


    @Test
    @SuppressWarnings("unchecked")
    public void asyncTests() {
        Future<List<Key>> jsonArrayResponseFuture = connection.executeAsync(new KeyList());
        List<Key> keys = (List<Key>)jsonArrayResponseFuture.get(Duration.fromTimeUnit(10L, TimeUnit.SECONDS)).get();
        assertNotNull(keys);
    }

}
