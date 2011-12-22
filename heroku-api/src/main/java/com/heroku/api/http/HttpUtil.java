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

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HttpUtil {

    private static String ENCODE_FAIL = "Unsupported encoding exception while encoding parameters";

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
            throw new RuntimeException("The URL was malformed: "+url);
        }
    }

    public static HerokuAPIException invalidLogin() {
        return new HerokuAPIException("Unable to login");
    }

    public static HerokuAPIException invalidKeys() {
        return new HerokuAPIException("Unable to add keys.");
    }

    public static RequestFailedException insufficientPrivileges(int code, byte[] bytes) {
        return new RequestFailedException("Insufficient privileges.", code, bytes);
    }


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
            throw new HerokuAPIException("IOException while reading response");
        }
    }

    public static String getUTF8String(byte[] in) {
        try {
            return new String(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HerokuAPIException("Somehow UTF-8 is unsupported");
        }
    }
}
