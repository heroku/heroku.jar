package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.HerokuAPIConfig;
import com.heroku.api.LoginVerification;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.LoginRequest;
import com.heroku.api.request.Request;
import com.heroku.api.request.login.BasicAuthLogin;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.*;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.*;

import static com.heroku.api.Heroku.Config.ENDPOINT;

public class HttpClientConnection implements Connection<Future<?>>, ConnectionProvider<Future<?>> {

    private DefaultHttpClient httpClient = getHttpClient();
    private volatile ExecutorService executorService;
    private Object lock = new Object();
    private final String apiKey;

    public HttpClientConnection() {
        this.apiKey = null;
    }

    public HttpClientConnection(LoginRequest login) {
        LoginVerification loginVerification = execute(login);
        this.apiKey = loginVerification.getApiKey();
        setHttpClientCredentials(apiKey);
    }

    public HttpClientConnection(String apiKey) {
        this.apiKey = apiKey;
        setHttpClientCredentials(this.apiKey);
    }

    private void setHttpClientCredentials(String apiKey) {
        URL endpoint = HttpUtil.toURL(ENDPOINT.value);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(endpoint.getHost(), endpoint.getPort()),
                // the Basic Authentication scheme only expects an API key.
                new UsernamePasswordCredentials("", apiKey));
    }

    @Override
    public <T> Future<T> executeAsync(final Request<T> request) {
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return execute(request);
            }
        };
        return getExecutorService().submit(callable);

    }

    @Override
    public Connection<Future<?>> get(HerokuAPIConfig config) {
        if (config.getApiKey() != null) {
            return new HttpClientConnection(config.getApiKey());
        } else if (config.getUsername() != null && config.getPassword() != null) {
            return new HttpClientConnection(new BasicAuthLogin(config.getUsername(), config.getPassword()));
        }
        return null;
    }

    @Override
    public <T> T execute(Request<T> request) {
        try {
            HttpRequestBase message = getHttpRequestBase(request.getHttpMethod(), ENDPOINT.value + request.getEndpoint());
            message.setHeader(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
            message.setHeader(request.getResponseType().getHeaderName(), request.getResponseType().getHeaderValue());

            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            if (request.hasBody()) {
                ((HttpEntityEnclosingRequestBase) message).setEntity(new StringEntity(request.getBody()));
            }

            HttpResponse httpResponse = httpClient.execute(message);

            return request.getResponse(HttpUtil.getBytes(httpResponse.getEntity().getContent()), httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException("exception while executing request", e);
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

    protected DefaultHttpClient getHttpClient() {
        SSLSocketFactory ssf = new SSLSocketFactory(Heroku.herokuSSLContext());
        ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager();
        if (!Heroku.Config.ENDPOINT.isDefault()) {
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
        }
        return new DefaultHttpClient(ccm);
    }


    @Override
    public void close() {
        getExecutorService().shutdownNow();
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
