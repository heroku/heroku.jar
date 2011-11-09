package com.heroku.api.http;


public enum ContentType implements HttpHeader {
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    SSH_AUTHKEY("text/ssh-authkey");

    String value;

    ContentType(String val) {
        this.value = val;
    }

    @Override
    public String getHeaderName() {
        return CONTENT_TYPE;
    }

    @Override
    public String getHeaderValue() {
        return value;
    }

}
