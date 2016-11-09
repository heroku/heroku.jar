package com.heroku.api.connection;

import com.heroku.api.request.Request;

import java.util.Map;

/**
 * Asynchronous version of Connection
 *
 * @param <F> the type of 'Future' that this connection will return
 */
public interface AsyncConnection<F> extends Connection {


    /**
     * Asynchronously execute the given request with, using a different apiKey than the one associated with this connection
     *
     * @param request The request to execute
     * @param apiKey API key for Heroku
     * @param <T>     The Type of the Response when parsed by the request, returned by the Future
     * @return A future Response
     */

    <T> F executeAsync(Request<T> request, String apiKey);


    /**
     * Asynchronously execute the given request with, using a different apiKey than the one associated with this connection
     *
     * @param request The request to execute
     * @param apiKey API key for Heroku
     * @param <T>     The Type of the Response when parsed by the request, returned by the Future
     * @param extraHeaders any extra headers to send with the request
     * @return A future Response
     */

    <T> F executeAsync(Request<T> request, Map<String,String> extraHeaders, String apiKey);

}
