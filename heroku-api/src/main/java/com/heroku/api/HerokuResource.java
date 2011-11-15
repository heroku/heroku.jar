package com.heroku.api;

import com.heroku.api.command.AppCommand;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResource {
    Login("/login"),
    Apps("/apps"),
    App("/apps/%s"),
    Addons("/addons"),
    AppAddons(App.value + "/addons"),
    AppAddon(AppAddons.value + "/%s"),
    User("/user"),
    Key(User.value + "/keys/%s"),
    Keys(User.value + "/keys"),
    Collaborators(App.value + "/collaborators"),
    Collaborator(Collaborators.value + "/%s"),
    ConfigVars(App.value + "/config_vars"),
    ConfigVar(ConfigVars.value + "/%s"),
    Logs(App.value + "/logs"),
    Process(App.value + "/ps"),
    Restart(Process.value + "/restart"),
    Stop(Process.value + "/stop"),
    Scale(Process.value + "/scale");

    public final String value;
    
    HerokuResource(String value) {
        this.value = value;
    }
}
