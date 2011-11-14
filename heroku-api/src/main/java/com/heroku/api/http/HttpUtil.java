package com.heroku.api.http;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.exception.HerokuAPIException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HttpUtil {

    private static String ENCODE_FAIL = "Unsupported encoding exception while encoding parameters";

    public static String encodeParameters(CommandConfig config, HerokuRequestKey... keys) {

        StringBuilder encodedParameters = new StringBuilder();
        String separator = "";
        for (HerokuRequestKey key : keys) {
            if (config.get(key) != null) {
                encodedParameters.append(separator);
                encodedParameters.append(urlencode(key.queryParameter, ENCODE_FAIL));
                encodedParameters.append("=");
                encodedParameters.append(urlencode(config.get(key), ENCODE_FAIL));
                separator = "&";
            }
        }
        return new String(encodedParameters);

    }


    public static String urlencode(String toEncode, String messageIfFails) {
        try {
            return URLEncoder.encode(toEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(messageIfFails, e);
        }
    }


    public static UnsupportedOperationException noBody() {
        return new UnsupportedOperationException("This command does not have a body. Use hasBody() to check for a body.");
    }

    public static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("The URL was malformed");
        }
    }

    public static HerokuAPIException invalidLogin() {
        return new HerokuAPIException("Unable to login");
    }

    public static HerokuAPIException invalidKeys() {
        return new HerokuAPIException("Unable to add keys.");
    }

}
