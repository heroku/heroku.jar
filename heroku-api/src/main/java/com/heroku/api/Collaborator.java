package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Collaborator implements Serializable {

    private static final long serialVersionUID = 1L;

    String access;
    String email;

    public String getAccess() {
        return access;
    }

    private void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
