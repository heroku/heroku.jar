package com.heroku.api.connection;

import com.heroku.api.command.CommandResponse;

import java.util.concurrent.Future;


public class HttpClientFutureWrapper<R extends CommandResponse> extends FutureWrapper<Future<R>, R> {

    public HttpClientFutureWrapper(Future<R> toWrap) {
        super(toWrap);
    }

    @Override
    public Future<R> getFuture() {
        return future;
    }
}
