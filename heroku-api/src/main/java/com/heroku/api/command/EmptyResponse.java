package com.heroku.api.command;

import java.io.InputStream;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class EmptyResponse implements CommandResponse {

    public EmptyResponse(InputStream in) {
        CommandUtil.closeQuietly(in);
    }


    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public byte[] getRawData() {
        return new byte[0];
    }

    @Override
    public Object getData() {
        return null;
    }
}
