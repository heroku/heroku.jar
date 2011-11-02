package com.heroku.command;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuCommandStringResponse implements HerokuCommandResponse {

    private final String data;

    private final boolean success;

    public HerokuCommandStringResponse(String data, boolean success) {
        this.data = data;
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Object get(String key) {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }
}
