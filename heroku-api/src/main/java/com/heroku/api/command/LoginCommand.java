package com.heroku.api.command;


public interface LoginCommand extends Command<LoginResponse> {

    public static final String DEFAULT_ENDPOINT = "https://api.heroku.com";

    String getApiEndpoint();

}
