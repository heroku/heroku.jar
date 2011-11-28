package com.heroku.api.httpclient;

import com.google.inject.Inject;
import com.heroku.api.HttpClientModule;
import com.heroku.api.request.app.AppList;
import com.heroku.api.request.response.JsonArrayResponse;
import com.heroku.api.connection.HttpClientConnection;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

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
        Future<JsonArrayResponse> jsonArrayResponseFuture = connection.executeAsync(new AppList());
        JsonArrayResponse jsonArrayResponse = jsonArrayResponseFuture.get(10L, TimeUnit.SECONDS);
        Assert.assertTrue(jsonArrayResponse != null);
    }

}
