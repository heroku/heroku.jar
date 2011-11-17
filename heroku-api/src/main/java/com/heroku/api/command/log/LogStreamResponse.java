package com.heroku.api.command.log;


import com.heroku.api.exception.HerokuAPIException;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class LogStreamResponse {

    URL logStreamURL;

    public LogStreamResponse(URL streamUrl) {
        logStreamURL = streamUrl;
    }


    public URL getLogStreamURL() {
        return logStreamURL;
    }

    public InputStream openStream() {
        try {
            URLConnection urlConnection = logStreamURL.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                HttpsURLConnection https = (HttpsURLConnection) urlConnection;
                https.setSSLSocketFactory(getSslSocketFactory());
                https.setHostnameVerifier(gethHostnameVerifier());
            }
            urlConnection.connect();
            return urlConnection.getInputStream();

        } catch (IOException e) {
            throw new HerokuAPIException("IOException while opening log stream", e);
        }
    }


    private SSLSocketFactory getSslSocketFactory() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new HerokuAPIException("Cant getSslSocketFactory, NoSuchAlgorithmException", e);
        } catch (KeyManagementException e) {
            throw new HerokuAPIException("Cant getSslSocketFactory, KeyManagementException", e);
        }
    }

    private HostnameVerifier gethHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }

}