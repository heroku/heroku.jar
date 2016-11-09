package com.heroku.api.http;

import com.heroku.api.Heroku;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.request.RequestConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP related utilities.
 *
 * @author Naaman Newbold
 */
public class HttpUtil {

    private static String ENCODE_FAIL = "Unsupported encoding exception while encoding parameters";

    /**
     * URL encode request paramaters from a {@link RequestConfig}.
     * @param config Name/value pairs for a HTTP request.
     * @param keys List of keys in the config to encode.
     * @return A string representation of encoded name/value parameters.
     */
    public static String encodeParameters(RequestConfig config, Heroku.RequestKey... keys) {

        StringBuilder encodedParameters = new StringBuilder();
        String separator = "";
        for (Heroku.RequestKey key : keys) {
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

    // Characters to encode that URLEncoder doesn't encode.
    // See: http://docs.oracle.com/javase/6/docs/api/java/net/URLEncoder.html
    static final Map<String, String> specialChars = Collections.unmodifiableMap(new HashMap<String, String>(){{
        put(".", "%2e");
        put("-", "%2d");
        put("*", "%2a");
        put("_", "%5f");
    }});

    /**
     * Some calls in the Heroku API decode strings in a different way from URLEncoder. This is a method for handling those
     * special cases. First, urlencode() is called. Then, .-*_ are replaced with their hexadecimal equivalent.
     * @param toEncode string to encode
     * @return A string representation of encoded parameters.
     */
    public static String encodeIncludingSpecialCharacters(String toEncode) {
        String encoded = urlencode(toEncode, "Unable to urlencode " + toEncode);
        for (Map.Entry<String, String> s : specialChars.entrySet()) {
            encoded = encoded.replace(s.getKey(), s.getValue());
        }
        return encoded;
    }

    public static String urlencode(String toEncode, String messageIfFails) {
        try {
            return URLEncoder.encode(toEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(messageIfFails, e);
        }
    }


    public static UnsupportedOperationException noBody() {
        return new UnsupportedOperationException("This request does not have a body. Use hasBody() to check for a body.");
    }

    public static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("The URL was malformed: " + url, e);
        }
    }

    public static RequestFailedException insufficientPrivileges(int code, byte[] bytes) {
        return new RequestFailedException("Insufficient privileges.", code, bytes);
    }

    /**
     * Converts an {@link InputStream} to a byte array
     * @param in input stream to convert
     * @return byte array
     */
    public static byte[] getBytes(InputStream in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(os);
        ReadableByteChannel rbc = Channels.newChannel(in);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            while (rbc.read(byteBuffer) != -1) {
                byteBuffer.flip();
                wbc.write(byteBuffer);
                byteBuffer.clear();
            }
            wbc.close();
            rbc.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new HerokuAPIException("IOException while reading response", e);
        }
    }

    public static String getUTF8String(byte[] in) {
        try {
            return new String(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Somehow UTF-8 is unsupported", e);
        }
    }
}
