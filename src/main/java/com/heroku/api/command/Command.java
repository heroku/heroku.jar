package com.heroku.api.command;

import java.util.Map;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public interface Command {
    public enum HttpMethod {
        GET,
        PUT,
        POST,
        DELETE
    }

    public enum ResponseType {
        XML,
        JSON
    }

    HttpMethod getHttpMethod();

    String getEndpoint();

    boolean hasBody();

    String getBody();

    ResponseType getResponseType();

    Map<String, String> getHeaders();

    int getSuccessCode();

    CommandResponse getResponse(byte[] bytes, boolean success);

}
