package com.heroku.api;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResource {
    Login("/login"),
    Apps("/apps");

    public final String value;
    
    HerokuResource(String value) {
        this.value = value;
    }
}
