package com.heroku.api.connection;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.command.login.LoginResponse;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
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

public class HttpClientConnection implements Connection<Future<?>> {

    private DefaultHttpClient httpClient = getHttpClient();
    private volatile ExecutorService executorService;
    private Object lock = new Object();
    private final String apiKey;

    public HttpClientConnection(LoginCommand login) {
        LoginResponse loginResponse = executeCommand(login);
        this.apiKey = loginResponse.api_key();
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
    public <T> Future<T> executeCommandAsync(final Command<T> command) {
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return executeCommand(command);
            }
        };
        return getExecutorService().submit(callable);

    }

    @Override
    public <T> T executeCommand(Command<T> command) {
        try {
            HttpRequestBase message = getHttpRequestBase(command.getHttpMethod(), ENDPOINT.value + command.getEndpoint());
            message.setHeader(Heroku.ApiVersion.HEADER, String.valueOf(Heroku.ApiVersion.v2.version));
            message.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());

            for (Map.Entry<String, String> header : command.getHeaders().entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            if (command.hasBody()) {
                ((HttpEntityEnclosingRequestBase) message).setEntity(new StringEntity(command.getBody()));
            }

            HttpResponse httpResponse = httpClient.execute(message);

            return command.getResponse(HttpUtil.getBytes(httpResponse.getEntity().getContent()), httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException("exception while executing command", e);
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


}
