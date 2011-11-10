package com.heroku.api.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class EmptyResponse implements CommandResponse {
    private final boolean success;

    public EmptyResponse(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public byte[] getRawData() {
        return new byte[0];
    }
}
