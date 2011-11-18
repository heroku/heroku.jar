package com.heroku.api.finagle;

import com.google.inject.Inject;
import com.heroku.api.FinagleModule;
import com.heroku.api.command.app.AppList;
import com.heroku.api.command.response.JsonArrayResponse;
import com.heroku.api.connection.FinagleConnection;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import com.twitter.util.Try;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;


@Guice(modules = FinagleModule.class)
public class FinagleConnectionTest {

    @Inject
    FinagleConnection connection;


    @Test
    public void asyncTests() {
        Future<JsonArrayResponse> jsonArrayResponseFuture = connection.executeCommandAsync(new AppList());
        Try<JsonArrayResponse> jsonArrayResponseTry = jsonArrayResponseFuture.get(Duration.fromTimeUnit(10L, TimeUnit.SECONDS));
        Assert.assertTrue(jsonArrayResponseTry.isReturn());
    }

}
