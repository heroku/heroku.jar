package com.heroku.api.command.login;


import com.heroku.api.command.response.JsonMapResponse;

public class LoginResponse extends JsonMapResponse {

    public LoginResponse(byte[] in) {
        super(in);
    }

    public String api_key() {
        return super.get("api_key");
    }

    public String verified_at() {
        return super.get("verified_at");
    }

    public String id() {
        return super.get("id");
    }

    public String email() {
        return super.get("email");
    }

}
