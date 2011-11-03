package com.heroku.api;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResource {
    Login("/login"),
    Apps("/apps"),
    App("/apps/%s"),
    User("/user"),
    Key(User.value + "/keys/%s"),
    Keys(User.value + "/keys"),
    Collaborators(App.value + "/collaborators"),
    Collaborator(Collaborators.value + "/%s"),
    ConfigVars(App.value + "/config_vars");

    public final String value;
    
    HerokuResource(String value) {
        this.value = value;
    }
}
