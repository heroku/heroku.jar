package com.heroku;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResource {
    LOGIN ("/login");

    public final String value;
    
    HerokuResource(String value) {
        this.value = value;
    }
}
