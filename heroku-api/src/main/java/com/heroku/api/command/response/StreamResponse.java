package com.heroku.api.command.response;


import com.heroku.api.command.CommandResponse;

import java.io.InputStream;

public class StreamResponse implements CommandResponse {

    private InputStream in;

    public StreamResponse(InputStream in) {
        this.in = in;
    }

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException("use getData to get the inpust stream");
    }

    @Override
    public byte[] getRawData() {
        return new byte[0];
    }

    @Override
    public InputStream getData() {
        return in;
    }
}
