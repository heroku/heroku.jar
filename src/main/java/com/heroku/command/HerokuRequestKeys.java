package com.heroku.command;

/**
* TODO: Javadoc
*
* @author Naaman Newbold
*/
public enum HerokuRequestKeys {
    stack ("app[stack]"),
    remote ("remote"),
    timeout ("timeout"),
    addons ("addons"),
    requested ("requested"),
    beta ("beta"),
    name ("name"),
    app ("app");

    public final String queryParameter;

    HerokuRequestKeys(String queryParameter) {
        this.queryParameter = queryParameter;
    }
}
