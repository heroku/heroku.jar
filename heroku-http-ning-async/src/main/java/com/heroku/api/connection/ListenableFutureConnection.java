package com.heroku.api.connection;

import com.heroku.api.request.Request;
import org.asynchttpclient.ListenableFuture;


public interface ListenableFutureConnection extends AsyncConnection<ListenableFuture<?>> {

    @Override
    <T> ListenableFuture<T> executeAsync(Request<T> request, String apiKey);

    @Override
    <T> T execute(Request<T> request, String apiKey);
}
