package com.heroku.api;

import java.io.Serializable;

/**
 * @author Naaman Newbold
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    String name;
    String created_at;
    String verified_at;
    String last_login;
    boolean confirmed;
    String id;
    boolean verified;
    String confirmed_at;
    String email;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVerifiedAt() {
        return verified_at;
    }

    private void setVerified_at(String verified_at) {
        this.verified_at = verified_at;
    }

    public String getLastLogin() {
        return last_login;
    }

    private void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public boolean isVerified() {
        return verified;
    }

    private void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getConfirmedAt() {
        return confirmed_at;
    }

    private void setConfirmed_at(String confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
