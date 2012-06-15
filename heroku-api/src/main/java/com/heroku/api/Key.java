package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Key implements Serializable {

    private static final long serialVersionUID = 1L;

    String email;
    String contents;

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getContents() {
        return contents;
    }

    private void setContents(String contents) {
        this.contents = contents;
    }
}
