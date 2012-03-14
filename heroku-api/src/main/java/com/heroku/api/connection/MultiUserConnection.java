package com.heroku.api.connection;


import com.heroku.api.request.Request;

public interface MultiUserConnection {

    <T> T execute(Request<T> request, String apiKey);

}
