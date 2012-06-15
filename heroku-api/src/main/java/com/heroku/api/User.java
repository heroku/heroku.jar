package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    String created_at;
    String last_login;
    String id;
    String email;

    public String getCreatedAt() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLastLogin() {
        return last_login;
    }

    private void setLast_login(String last_login) {
        this.last_login = last_login;
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
}
