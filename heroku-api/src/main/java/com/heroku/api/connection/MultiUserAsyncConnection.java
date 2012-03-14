package com.heroku.api.connection;

import com.heroku.api.request.Request;


public interface MultiUserAsyncConnection<F>  {
    <T> F executeAsync(Request<T> request, String apiKey);
}
