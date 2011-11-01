package com.heroku.api;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HerokuStack {
    Aspen ("aspen"),
    Bamboo ("bamboo"),
    Cedar ("cedar");

    public final String value;

    HerokuStack(String value) {
        this.value = value;
    }
}
