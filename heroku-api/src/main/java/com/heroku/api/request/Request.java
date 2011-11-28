package com.heroku.api.request;

import com.heroku.api.http.Http;

import java.util.Map;

public interface Request<T> {


    Http.Method getHttpMethod();

    String getEndpoint();

    boolean hasBody();

    String getBody();

    Http.Accept getResponseType();

    Map<String, String> getHeaders();

    T getResponse(byte[] bytes, int status);

}
