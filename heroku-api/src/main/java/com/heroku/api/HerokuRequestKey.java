package com.heroku.api;

/**
* TODO: Javadoc
*
* @author Naaman Newbold
*/
public enum HerokuRequestKey {
    stack ("app[stack]"),
    createAppName("app[name]"),
    remote ("remote"),
    timeout ("timeout"),
    addons ("addons"),
    requested ("requested"),
    beta ("beta"),
    appName("name"),
    sshkey ("sshkey"),
    config("config"),
    collaborator("collaborator[email]"),
    transferOwner("app[transfer_owner]"),
    configvars("config_vars"),
    configVarName("key"),
    username("username"),
    password("password");

    public final String queryParameter;

    HerokuRequestKey(String queryParameter) {
        this.queryParameter = queryParameter;
    }
}
