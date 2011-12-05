package com.heroku.api;


import com.heroku.api.connection.Connection;
import com.heroku.api.model.Addon;
import com.heroku.api.model.App;
import com.heroku.api.model.Key;
import com.heroku.api.request.addon.AddonList;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppList;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;

import java.util.List;

public class HerokuAPI {

    Connection<?> connection;

    public static HerokuAPI connect(Connection<?> connection) {
        return new HerokuAPI(connection);
    }

    protected HerokuAPI(Connection<?> connection) {
        this.connection = connection;
    }

    public void addKey(String sshKey) {
        connection.execute(new KeyAdd(sshKey));
    }

    public void removeKey(String sshKey) {
        connection.execute(new KeyRemove(sshKey));
    }

    public List<Key> listKeys() {
        return connection.execute(new KeyList());
    }
    
    public List<Addon> listAddons() {
        return connection.execute(new AddonList());
    }

    public List<App> apps() {
        return connection.execute(new AppList());
    }

    public HerokuAppAPI app(String name) {
        return new HerokuAppAPI(connection, name);
    }

    public HerokuAppAPI newapp(Heroku.Stack stack) {
        return new HerokuAppAPI(connection, connection.execute(new AppCreate(stack)).getName());
    }

    public HerokuAppAPI newapp(Heroku.Stack stack, String appName) {
        return new HerokuAppAPI(connection, connection.execute(new AppCreate(stack).withName(appName)).getName());
    }


}
