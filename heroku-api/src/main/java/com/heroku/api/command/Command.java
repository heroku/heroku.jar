package com.heroku.api.command;

import com.heroku.api.http.Http;

import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface Command<T> {


    Http.Method getHttpMethod();

    String getEndpoint();

    boolean hasBody();

    String getBody();

    Http.Accept getResponseType();

    Map<String, String> getHeaders();

    T getResponse(byte[] bytes, int status);

}
