package com.heroku.api.command;


public class LoginResponse extends JsonMapResponse {

    public LoginResponse(byte[] data, boolean success) {
        super(data, success);
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
