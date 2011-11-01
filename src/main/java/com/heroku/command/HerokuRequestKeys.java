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
    app ("app"),
    sshkey ("sshkey"),
    config("config"),
    collaborator("collaborator[email]"),
    configvars("config_vars");

    public final String queryParameter;

    HerokuRequestKeys(String queryParameter) {
        this.queryParameter = queryParameter;
    }
}
