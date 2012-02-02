package com.heroku.api.httpclient;

import com.google.inject.Inject;
import com.heroku.api.App;
import com.heroku.api.HttpClientModule;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.request.app.AppList;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Guice(modules = HttpClientModule.class)
public class HttpClientConnectionTest {

    @Inject
    HttpClientConnection connection;

    @Test
    public void asyncTests() throws ExecutionException, TimeoutException, InterruptedException {
        Future<List<App>> jsonArrayResponseFuture = connection.executeAsync(new AppList());
        List<App> jsonArrayResponse = jsonArrayResponseFuture.get(10L, TimeUnit.SECONDS);
        Assert.assertTrue(jsonArrayResponse != null);
    }

}
