package com.heroku.api.finagle;

import com.google.inject.Inject;
import com.heroku.api.FinagleModule;
import com.heroku.api.model.App;
import com.heroku.api.request.app.AppList;
import com.heroku.api.connection.FinagleConnection;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import com.twitter.util.Try;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Guice(modules = FinagleModule.class)
public class FinagleConnectionTest {

    @Inject
    FinagleConnection connection;


    @Test
    public void asyncTests() {
        Future<List<App>> jsonArrayResponseFuture = connection.executeAsync(new AppList());
        Try<List<App>> jsonArrayResponseTry = jsonArrayResponseFuture.get(Duration.fromTimeUnit(10L, TimeUnit.SECONDS));
        Assert.assertTrue(jsonArrayResponseTry.isReturn());
    }

}
