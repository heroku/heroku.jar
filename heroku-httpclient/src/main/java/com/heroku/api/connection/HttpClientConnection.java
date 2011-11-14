package com.heroku.api.connection;

import com.heroku.api.HerokuAPI;
import com.heroku.api.command.*;
import com.heroku.api.http.HerokuApiVersion;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HttpClientConnection implements Connection<Future<?>> {

    private LoginCommand loginCommand;
    private LoginResponse loginResponse;
    private URL endpoint;
    private DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
    private volatile ExecutorService executorService;
    private Object lock = new Object();

    public HttpClientConnection(LoginCommand login) {
        this.loginCommand = login;
        this.endpoint = HttpUtil.toURL(loginCommand.getApiEndpoint());
        this.loginResponse = executeCommand(loginCommand);
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(endpoint.getHost(), endpoint.getPort()),
                // the Basic Authentication scheme only expects an API key.
                new UsernamePasswordCredentials("", loginResponse.api_key()));

    }

    @Override
    public <T extends CommandResponse> Future<T> executeCommandAsync(final Command<T> command) {
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return executeCommand(command);
            }
        };
        return getExecutorService().submit(callable);

    }

    @Override
    public <T extends CommandResponse> T executeCommand(Command<T> command) {
        try {
            HttpRequestBase message = getHttpRequestBase(command.getHttpMethod(), CommandUtil.getCommandEndpoint(endpoint, command.getEndpoint()).toString());
            message.setHeader(HerokuApiVersion.HEADER, String.valueOf(HerokuApiVersion.v2.version));
            message.setHeader(command.getResponseType().getHeaderName(), command.getResponseType().getHeaderValue());

            for (Map.Entry<String, String> header : command.getHeaders().entrySet()) {
                message.setHeader(header.getKey(), header.getValue());
            }

            if (command.hasBody()) {
                ((HttpEntityEnclosingRequestBase) message).setEntity(new StringEntity(command.getBody()));
            }

            HttpResponse httpResponse = httpClient.execute(message);

            return command.getResponse(httpResponse.getEntity().getContent(), httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException("exception while executing command", e);
        }
    }


    private HttpRequestBase getHttpRequestBase(Method httpMethod, String endpoint) {
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

    @Override
    public HerokuAPI getApi() {
        return new HerokuAPI(this);
    }

    @Override
    public URL getEndpoint() {
        return endpoint;
    }

    @Override
    public String getEmail() {
        return loginResponse.email();
    }

    @Override
    public String getApiKey() {
        return loginResponse.api_key();
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
        return Executors.newCachedThreadPool();
    }


}
