package com.heroku.api.connection;


import com.heroku.api.command.CommandResponse;

public abstract class FutureWrapper<R extends CommandResponse, F> {

    protected F future;

    public FutureWrapper(F toWrap) {
        future = toWrap;
    }

    public abstract Object getFuture();

}
