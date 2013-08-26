package com.heroku.api.connection;

import com.heroku.api.request.Request;

import java.util.Map;
import java.util.concurrent.Future;


public interface FutureConnection extends AsyncConnection<Future<?>> {
    @Override
    <T> Future<T> executeAsync(Request<T> request, String apiKey);

    @Override
    <T> T execute(Request<T> request, String apiKey);

    <T> T execute(Request<T> request, Map<String,String> extraHeaders, String apiKey);

    <T> Future<T> executeAsync(Request<T> request, Map<String,String> extraHeaders, String apiKey);

}
