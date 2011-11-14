package com.heroku.api.command;


import java.io.InputStream;

public class TextResponse implements CommandResponse {

    String response;

    public TextResponse(InputStream in) {
        response = CommandUtil.getString(in);
    }

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException("call getData to get the text of the response");
    }

    @Override
    public byte[] getRawData() {
        return response.getBytes();
    }

    @Override
    public String getData() {
        return response;
    }
}
