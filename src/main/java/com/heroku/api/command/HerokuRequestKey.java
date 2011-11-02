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
    sshkey ("sshkey"),
    config("config"),
    collaborator("collaborator[email]"),
    configvars("config_vars");

    public final String queryParameter;

    HerokuRequestKey(String queryParameter) {
        this.queryParameter = queryParameter;
    }
}
