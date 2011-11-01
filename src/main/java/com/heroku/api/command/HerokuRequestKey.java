package com.heroku.api.command;

/**
* TODO: Javadoc
*
* @author Naaman Newbold
*/
public enum HerokuRequestKey {
    stack ("app[stack]"),
    remote ("remote"),
    timeout ("timeout"),
    addons ("addons"),
    requested ("requested"),
    beta ("beta"),
    name ("name"),
    app ("app");

    public final String queryParameter;

    HerokuRequestKey(String queryParameter) {
        this.queryParameter = queryParameter;
    }
}
