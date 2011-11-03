package com.heroku.api;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResource {
    Login("/login"),
    Apps("/apps"),
    App("/apps/%s");

    public final String value;
    
    HerokuResource(String value) {
        this.value = value;
    }
}
