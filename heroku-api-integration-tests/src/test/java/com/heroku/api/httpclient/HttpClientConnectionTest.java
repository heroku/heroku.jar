package com.heroku.api.httpclient;

import com.heroku.api.App;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.request.app.AppList;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class HttpClientConnectionTest {

    HttpClientConnection connection = new HttpClientConnection();

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Test
    public void asyncTests() throws ExecutionException, TimeoutException, InterruptedException {
        Future<? extends List<App>> jsonArrayResponseFuture = connection.executeAsync(new AppList(), apiKey);
        List<App> jsonArrayResponse = jsonArrayResponseFuture.get(10L, TimeUnit.SECONDS);
        Assert.assertTrue(jsonArrayResponse != null);
    }
}
