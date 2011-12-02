package com.heroku.api.model;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Collaborator {
    String access;
    String email;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
