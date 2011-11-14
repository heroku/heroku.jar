package com.heroku.api.command;


public class ChainedResponse<T extends Command> implements CommandResponse {

    T nextCommand;
    byte[] data;

    public ChainedResponse(T next, byte[] raw) {
        nextCommand = next;
        data = raw;
    }

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException("Call getData to get the next command to execute");
    }

    @Override
    public byte[] getRawData() {
        return data;
    }

    @Override
    public T getData() {
        return nextCommand;
    }
}
