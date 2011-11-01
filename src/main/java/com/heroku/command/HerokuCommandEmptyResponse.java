package com.heroku.command;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuCommandEmptyResponse implements HerokuCommandResponse {
    private final boolean success;

    public HerokuCommandEmptyResponse(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Object get(String key) {
        return null;
    }
}
