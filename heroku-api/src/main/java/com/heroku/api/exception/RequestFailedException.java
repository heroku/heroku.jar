package com.heroku.api.exception;


import com.heroku.api.http.HttpUtil;

/**
 * Exception for {@link com.heroku.api.request.Request} failures. These include, but are not limited to
 * {@link java.io.IOException} and errors from the API.
 */
public class RequestFailedException extends HerokuAPIException {

    String responseBody;
    int statusCode;

    public RequestFailedException(String msg, int code, byte[] in) {
        this(msg, code, getBodyFromInput(in));

    }

    private static String getBodyFromInput(byte[] in) {
        try {
            return HttpUtil.getUTF8String(in);
        } catch (Exception e) {
            return "There was also an error reading the response body.";
        }
    }

    public RequestFailedException(String msg, int code, String body) {
        super(msg + " statuscode:" + code + " responseBody:" + body);
        responseBody = body;
        statusCode = code;
    }


    public String getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
