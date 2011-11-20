package com.heroku.api;


import com.heroku.api.command.app.AppCreate;
import com.heroku.api.command.key.KeyAdd;
import com.heroku.api.command.key.KeyRemove;
import com.heroku.api.connection.Connection;

public class HerokuAPI {

    Connection<?> connection;

    public static HerokuAPI with(Connection<?> connection) {
        return new HerokuAPI(connection);
    }

    protected HerokuAPI(Connection<?> connection) {
        this.connection = connection;
    }

    public void addKey(String sshKey) {
        connection.executeCommand(new KeyAdd(sshKey));
    }

    public void removeKey(String sshKey) {
        connection.executeCommand(new KeyRemove(sshKey));
    }

    public HerokuAppAPI app(String name) {
        return new HerokuAppAPI(connection, name);
    }

    public HerokuAppAPI newapp(Heroku.Stack stack) {
        return new HerokuAppAPI(connection, connection.executeCommand(new AppCreate(stack)).get("name"));
    }

    public HerokuAppAPI newapp(Heroku.Stack stack, String appName) {
        return new HerokuAppAPI(connection, connection.executeCommand(new AppCreate(stack).withName(appName)).get("name"));
    }


}
