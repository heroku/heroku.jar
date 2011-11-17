package com.heroku.api.command;

import com.heroku.api.http.Accept;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface Command<T> {


    Method getHttpMethod();

    String getEndpoint();

    boolean hasBody();

    String getBody();

    Accept getResponseType();

    Map<String, String> getHeaders();

    T getResponse(InputStream inputStream, int status);

}
