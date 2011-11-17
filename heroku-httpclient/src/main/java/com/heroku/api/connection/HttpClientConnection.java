package com.heroku.api.connection;

import com.heroku.api.HerokuAPI;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandResponse;
import com.heroku.api.command.LoginCommand;
import com.heroku.api.command.login.LoginResponse;
import com.heroku.api.http.HerokuApiVersion;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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
    private DefaultHttpClient httpClient = wrapClient(new DefaultHttpClient(new ThreadSafeClientConnManager()));
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
            HttpRequestBase message = getHttpRequestBase(command.getHttpMethod(), getCommandEndpoint(endpoint, command.getEndpoint()).toString());
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

    private static URL getCommandEndpoint(URL connectionEndpoint, String commandEndpoint) {
        if (commandEndpoint.startsWith("http://") || commandEndpoint.startsWith("https://")) {
            return HttpUtil.toURL(commandEndpoint);
        } else {
            return HttpUtil.toURL(connectionEndpoint.toString() + commandEndpoint);
        }

    }

    public static DefaultHttpClient wrapClient(DefaultHttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            return null;
        }
    }


}
