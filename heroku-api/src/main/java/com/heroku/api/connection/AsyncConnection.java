package com.heroku.api.connection;

import com.heroku.api.request.Request;

public interface AsyncConnection<F> extends Connection {

    <T> F executeAsync(Request<T> request);

}
