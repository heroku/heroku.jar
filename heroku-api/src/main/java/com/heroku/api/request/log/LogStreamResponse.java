package com.heroku.api.request.log;


import com.heroku.api.Heroku;
import com.heroku.api.exception.HerokuAPIException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Handles {@link Log} responses. When a log is requested, a URL is returned to <a href="https://github.com/heroku/logplex">Logplex</a>.
 *
 * To begin reading logs, use {@link #openStream()}. {@link #getLogStreamURL()} can also be used directly to create a custom connection to Logplex.
 */
public class LogStreamResponse {

    URL logStreamURL;

    public LogStreamResponse(URL streamUrl) {
        logStreamURL = streamUrl;
    }

    /**
     * A logplex session URL. Using Logplex directly will generally require you to add logplex's certificate to java's keystore.
     * @return url to the log stream
     */
    public URL getLogStreamURL() {
        return logStreamURL;
    }

    /**
     * Creates a {@link URLConnection} to logplex. SSL verification is not used because Logplex's certificate is not signed by an authority that exists
     * in the standard java keystore.
     * @return input stream
     */
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