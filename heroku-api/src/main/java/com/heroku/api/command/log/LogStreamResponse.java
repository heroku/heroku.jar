package com.heroku.api.command.log;


import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.HttpUtil;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, HttpUtil.trustAllTrustManager(), new java.security.SecureRandom());
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