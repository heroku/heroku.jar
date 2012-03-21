package com.heroku.api.httpclient;

import com.google.inject.Inject;
import com.heroku.api.App;
import com.heroku.api.HttpClientModule;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.connection.HttpClientConnection;
import com.heroku.api.http.Http;
import com.heroku.api.request.addon.AddonList;
import com.heroku.api.request.app.AppList;
import mockit.*;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Guice(modules = HttpClientModule.class)
public class HttpClientConnectionTest {

    HttpClientConnection connection = new HttpClientConnection();

    String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Test
    public void asyncTests() throws ExecutionException, TimeoutException, InterruptedException {
        Future<List<App>> jsonArrayResponseFuture = connection.executeAsync(new AppList(), apiKey);
        List<App> jsonArrayResponse = jsonArrayResponseFuture.get(10L, TimeUnit.SECONDS);
        Assert.assertTrue(jsonArrayResponse != null);
    }

    @Test
    public void userAgentShouldContainHerokuJarWithVersionNumber() {
        new MockUp<AbstractHttpClient>() {
            @Mock
            public final HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
                Assert.assertEquals(request.getHeaders(Http.UserAgent.LATEST.getHeaderName())[0].getValue(), Http.UserAgent.LATEST.getHeaderValue("httpclient"));

                BasicHttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
                basicHttpResponse.setEntity(new StringEntity("[]"));
                return basicHttpResponse;
            }
        };
        connection.execute(new AddonList(), apiKey);
        Mockit.tearDownMocks(AbstractHttpClient.class);
    }

    @Test
    public void cookiesShouldBeIgnored() {
        new MockUp<AbstractHttpClient>() {
            @Mock
            public final HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
                Assert.assertEquals(request.getHeaders("Cookie").length, 0, "Cookies should be ignored, but there are cookies present.");
                BasicHttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
                basicHttpResponse.setHeader("Set-Cookie", "foo=bar; path=/;");
                basicHttpResponse.setEntity(new StringEntity("[]"));
                return basicHttpResponse;
            }
        };
        connection.execute(new AddonList(), apiKey);
        // run this twice to ensure the set-cookie was sent from the first request
        connection.execute(new AddonList(), apiKey);
        Mockit.tearDownMocks(AbstractHttpClient.class);
    }
}
