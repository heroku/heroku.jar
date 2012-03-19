package com.heroku.api.connection;

import com.heroku.api.request.Request;

/**
 * Connection manager for connecting to the Heroku API.
 *
 * @author Naaman Newbold
 */
public interface Connection {

    /**
     * Synchronously execute the given request
     *
     * @param request the request to execute
     * @param <T>     the response type
     * @return the response as parsed by the request object
     */
    <T> T execute(Request<T> request);


    /**
     * Synchronously execute the given request, using a different apiKey than the one associated with this connection
     *
     * @param request the request to execute
     * @param <T>     the response type
     * @return the response as parsed by the request object
     */
    <T> T execute(Request<T> request, String apiKey);

    /**
     * Get the apiKey associated with this connection
     *
     * @return
     */
    String getApiKey();

    /**
     * Release any resources associated with this connection
     */
    void close();

}
