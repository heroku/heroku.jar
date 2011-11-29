package com.heroku.api.asynchttpclient;

import com.google.inject.Inject;
import com.heroku.api.AsyncHttpClientModule;
import com.heroku.api.model.App;
import com.heroku.api.request.app.AppList;
import com.heroku.api.connection.AsyncHttpClientConnection;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Guice(modules = AsyncHttpClientModule.class)
public class AsyncHttpClientConnectionTest {

    @Inject
    AsyncHttpClientConnection connection;

    @Test
    public void asyncTests() throws ExecutionException, TimeoutException, InterruptedException {
        Future<List<App>> jsonArrayResponseFuture = connection.executeAsync(new AppList());
        List<App> jsonArrayResponse = jsonArrayResponseFuture.get(10L, TimeUnit.SECONDS);
        Assert.assertTrue(jsonArrayResponse != null);
    }

}
