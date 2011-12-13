package com.heroku.api;

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
