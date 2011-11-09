package com.heroku.api.command;

import com.heroku.api.http.Accept;
import com.heroku.api.http.Method;

import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface Command {


    Method getHttpMethod();

    String getEndpoint();

    boolean hasBody();

    String getBody();

    Accept getResponseType();

    Map<String, String> getHeaders();

    int getSuccessCode();

    CommandResponse getResponse(byte[] bytes, boolean success);

}
