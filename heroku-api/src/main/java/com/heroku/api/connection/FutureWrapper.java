package com.heroku.api.connection;


public abstract class FutureWrapper<F> {

    protected F future;

    public FutureWrapper(F toWrap) {
        future = toWrap;
    }

    public abstract Object getFuture();

}
