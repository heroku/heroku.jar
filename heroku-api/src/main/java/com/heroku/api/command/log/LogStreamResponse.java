package com.heroku.api.command.log;


import com.heroku.api.Heroku;
import com.heroku.api.exception.HerokuAPIException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
                https.setSSLSocketFactory(Heroku.sslContext(false).getSocketFactory());
                https.setHostnameVerifier(Heroku.hostnameVerifier(false));
            }
            urlConnection.connect();
            return urlConnection.getInputStream();

        } catch (IOException e) {
            throw new HerokuAPIException("IOException while opening log stream", e);
        }
    }
}