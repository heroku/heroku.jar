package com.heroku.api.http;


public enum Accept implements HttpHeader {
    JSON("application/json"),
    XML("text/xml");

    String value;

    Accept(String val) {
        this.value = val;
    }

    @Override
    public String getHeaderName() {
        return ACCEPT;
    }

    @Override
    public String getHeaderValue() {
        return value;
    }

}
