package com.heroku.api.request;

import com.heroku.api.http.Http;

import java.util.Map;

/**
 * Request model for an HTTP request to the Heroku API. Intended for use with a {@link com.heroku.api.connection.Connection}.
 *
 * Models an HTTP request relative to a base URL (e.g. https://api.heroku.com).
 *
 * @param <T> Response type.
 */
public interface Request<T> {


    /**
     * HTTP method. e.g. GET, POST, PUT, DELETE
     * @return The HTTP method used in the request.
     */
    Http.Method getHttpMethod();

    /**
     * Path and query parameters of a URL.
     * @return The path and query parameters as a String.
     */
    String getEndpoint();

    /**
     * Whether or not the request has a body.
     * @return true if it has a request body, otherwise false
     */
    boolean hasBody();

    /**
     * Value of the request body.
     * @return Body
     * @throws UnsupportedOperationException Generally thrown if {@link #hasBody()} returns false
     */
    String getBody();

    /**
     * Value of the request body as a Map.
     * @return Body
     * @throws UnsupportedOperationException Generally thrown if {@link #hasBody()} returns false
     */
    Map<String, ?> getBodyAsMap();

    /**
     * HTTP Accept header.
     * @return The Accept header to be used in the request. Typically "application/json" or "text/xml"
     * @see com.heroku.api.http.Http.Accept
     */
    Http.Accept getResponseType();

    /**
     * {@link Map} of request-specific HTTP headers.
     * @return Name/value pairs of HTTP headers.
     */
    Map<String, String> getHeaders();

    /**
     * Response handler.
     * @param bytes Data returned from the request.
     * @param status HTTP status code.
     * @return The type {@link T} as specified by an individual request.
     * @throws com.heroku.api.exception.RequestFailedException Generally thrown when the HTTP status code is 4XX.
     */
    T getResponse(byte[] bytes, int status, Map<String,String> headers);

}
