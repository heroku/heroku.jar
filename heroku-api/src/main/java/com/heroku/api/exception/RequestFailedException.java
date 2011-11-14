package com.heroku.api.exception;


import com.heroku.api.command.CommandUtil;

import java.io.InputStream;

public class RequestFailedException extends HerokuAPIException {

    String responseBody;
    int statusCode;

    public RequestFailedException(String msg, int code, InputStream in) {
        this(msg, code, getBodyFromInput(in));

    }

    private static String getBodyFromInput(InputStream in) {
        try {
            return CommandUtil.getString(in);
        } catch (Exception e) {
            return "There was also an error reading the response body.";
        }
    }

    public RequestFailedException(String msg, int code, String body) {
        super(msg);
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
