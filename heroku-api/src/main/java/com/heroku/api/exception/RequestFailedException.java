package com.heroku.api.exception;


public class RequestFailedException extends HerokuAPIException {

    String responseBody;
    int statusCode;

    public RequestFailedException(String msg, int code, byte[] body) {
        super(failMessage(msg, code, body));
        responseBody = new String(body);
        statusCode = code;
    }

    public static String failMessage(String msg, int code, byte[] body) {
        String bodyStr = new String(body);
        return String.format("%s code %d, body => %s", msg, code, bodyStr);
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
