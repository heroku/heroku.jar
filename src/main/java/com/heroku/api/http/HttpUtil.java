package com.heroku.api.http;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.command.CommandConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HttpUtil {
    public static String encodeParameters(CommandConfig config, HerokuRequestKey... keys) {
        try {
            StringBuilder encodedParameters = new StringBuilder();
            String separator = "";
            for (HerokuRequestKey key : keys) {
                if (config.get(key) != null) {
                    encodedParameters.append(separator);
                    encodedParameters.append(
                            URLEncoder.encode(key.queryParameter, "UTF-8")
                    );
                    encodedParameters.append("=");
                    encodedParameters.append(
                            URLEncoder.encode(config.get(key), "UTF-8")
                    );
                    separator = "&";
                }
            }
            return new String(encodedParameters);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding exception while encoding parameters", e);
        }
    }

    public static UnsupportedOperationException noBody() {
        return new UnsupportedOperationException("This command does not have a body. Use hasBody() to check for a body.");
    }
}
