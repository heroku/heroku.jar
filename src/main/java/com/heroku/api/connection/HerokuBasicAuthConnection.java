package com.heroku.api.connection;

import com.heroku.api.command.HerokuCommand;
import org.apache.http.HttpMessage;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuBasicAuthConnection implements HerokuConnection {

    private final URL endpoint;
    private final String apiKey;
    private final String userId;
    private final String userEmail;

    public HerokuBasicAuthConnection(URL endpoint, String apiKey, String userId, String userEmail) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    @Override
    public void executeCommand(HerokuCommand command) throws IOException {
        command.execute(this);
    }

    @Override
    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                new AuthScope(endpoint.getHost(), endpoint.getPort()),
                new UsernamePasswordCredentials(userEmail, apiKey)
        );

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public void checkServerTrusted(
                            X509Certificate[] chain,String authType) throws CertificateException { }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() { return null; }

                } }, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
//            ClientConnectionManager cm = client.getConnectionManager();
//            SchemeRegistry schemeRegistry = cm.getSchemeRegistry();
//            schemeRegistry.register(new Scheme("https", sf, 443));
//            schemeRegistry.register(new Scheme("http", sf, 80));
//            HttpHost proxy = new HttpHost("127.0.0.1", 8080);
//            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        } catch (Exception e) {
            System.out.println("SSL setup error:");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return client;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T extends HttpMessage> T getHttpMethod(T method) {
        if (method == null)
            throw new IllegalArgumentException("The HttpMethod cannot be null.");

        method.addHeader("X-Heroku-API-Version", "2");
        return method;
    }

    @Override
    public URL getEndpoint() {
        return endpoint;
    }

    @Override
    public String getEmail() {
        return userEmail;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}
