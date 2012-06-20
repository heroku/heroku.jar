package com.heroku.api;

import java.io.Serializable;

public class LoginVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    String api_key;
    String verified_at;
    String id;
    String email;
    boolean verified;

    public String getApiKey() {
        return api_key;
    }

    private void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getVerifiedAt() {
        return verified_at;
    }

    private void setVerified_at(String verified_at) {
        this.verified_at = verified_at;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    private void setVerified(boolean verified) {
        this.verified = verified;
    }
}
