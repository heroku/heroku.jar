package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.heroku.api.Heroku.Config.ENDPOINT;

public class HttpClientConnection implements FutureConnection {


    private URL endpoint = HttpUtil.toURL(ENDPOINT.value);
    private CloseableHttpClient httpClient = createClient();
    private volatile ExecutorService executorService;
    private Object lock = new Object();

    public HttpClientConnection() {
    }

    @Override
    public <T> Future<T> executeAsync(final Request<T> request,  final String apiKey) {
        return executeAsync(request, Collections.<String,String>emptyMap(), apiKey);
    }

    @Override
    public <T> Future<T> executeAsync(final Request<T> request, final Map<String,String> exraHeaders, final String apiKey) {

        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return execute(request, exraHeaders, apiKey);
            }
        };
        return getExecutorService().submit(callable);

    }

    @Override
    public <T> T execute(Request<T> request, String key) {
        return execute(request, Collections.<String,String>emptyMap(), key);
    }

    @Override
    public <T> T execute(Request<T> request, Map<String,String> exraHeaders, String key) {
        try {
            HttpRequestBase message = getHttpRequestBase(request.getHttpMethod(), ENDPOINT.value + request.getEndpoint());
            message.setHeader(Heroku.ApiVersion.v3.getHeaderName(), Heroku.ApiVersion.v3.getHeaderValue());
            message.setHeader(Http.ContentType.JSON.getHeaderName(), Http.ContentType.JSON.getHeaderValue());
            message.setHeader(Http.UserAgent.LATEST.getHeaderName(), Http.UserAgent.LATEST.getHeaderValue("httpclient"));

            for (Map.Entry<String, String> header : exraHeaders.entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            if (request.hasBody()) {
                ((HttpEntityEnclosingRequestBase) message).setEntity(new StringEntity(request.getBody(), "UTF-8"));
            }

            if (key != null) {
                message.setHeader("Authorization", "Basic " + Base64.encodeBase64String((":" + key).getBytes()));
            }

            BasicHttpContext ctx = new BasicHttpContext();
            ctx.setAttribute("preemptive-auth", new BasicScheme());

            HttpResponse httpResponse = httpClient.execute(message, ctx);

            return request.getResponse(HttpUtil.getBytes(httpResponse.getEntity().getContent()), httpResponse.getStatusLine().getStatusCode(), toJavaMap(httpResponse.getAllHeaders()));
        } catch (IOException e) {
            throw new RuntimeException("Exception while executing request", e);
        }
    }

    private HttpRequestBase getHttpRequestBase(Http.Method httpMethod, String endpoint) {
        switch (httpMethod) {
            case GET:
                return new HttpGet(endpoint);
            case PUT:
                return new HttpPut(endpoint);
            case POST:
                return new HttpPost(endpoint);
            case DELETE:
                return new HttpDelete(endpoint);
            case PATCH:
                return new HttpPatch(endpoint);
            default:
                throw new UnsupportedOperationException(httpMethod + " is not a supported request type.");
        }
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (lock) {
                if (executorService == null) {
                    executorService = createExecutorService();
                }
            }
        }
        return executorService;
    }

    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            }
        });
    }

    protected static CloseableHttpClient createClientWithProxy(String proxyStr) throws URISyntaxException {
        URI proxyUri = new URI(proxyStr);
        HttpHost proxy = new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return HttpClients.custom()
            .useSystemProperties()
            .setRoutePlanner(routePlanner)
            .build();
    }

    protected static CloseableHttpClient createClient() {
        String httpProxy = System.getenv("HTTP_PROXY");
        String httpsProxy = System.getenv("HTTPS_PROXY");

        if (httpsProxy != null) {
            try {
                return createClientWithProxy(httpsProxy);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("HTTPS_PROXY is not valid!" , e);
            }
        } else if (httpProxy != null) {
            try {
                return createClientWithProxy(httpProxy);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("HTTP_PROXY is not valid!" , e);
            }
        } else {
            return HttpClients.createSystem();
        }
    }

    protected Map<String,String> toJavaMap(Header[] headers) {
        Map<String,String> javaMapHeaders = new HashMap<>();
        for (Header h : headers) {
            javaMapHeaders.put(h.getName(), h.getValue());
        }
        return javaMapHeaders;
    }


    @Override
    public void close() {
        getExecutorService().shutdownNow();
    }


    public static class Provider implements ConnectionProvider {

        @Override
        public Connection getConnection() {
            return new HttpClientConnection();
        }
    }

    static class PreemptiveAuth implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme available yet, try to initialize it preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
                        ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(
                            new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }

            }
        }
    }
}
