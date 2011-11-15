package com.heroku.api;


import com.heroku.api.command.app.AppCreateCommand;
import com.heroku.api.command.key.KeysAddCommand;
import com.heroku.api.command.key.KeysRemoveCommand;
import com.heroku.api.connection.Connection;

public class HerokuAPI {

    Connection<?> connection;

    public HerokuAPI(Connection<?> connection) {
        this.connection = connection;
    }

    public void addKey(String sshKey) {
        connection.executeCommand(new KeysAddCommand(sshKey));
    }

    public void removeKey(String sshKey) {
        connection.executeCommand(new KeysRemoveCommand(sshKey));
    }

    public HerokuAppAPI app(String name) {
        return new HerokuAppAPI(connection, name);
    }

    public HerokuAppAPI newapp(HerokuStack stack) {
        return new HerokuAppAPI(connection, connection.executeCommand(new AppCreateCommand(stack.value)).get("name"));
    }

    public HerokuAppAPI newapp(HerokuStack stack, String appName) {
        return new HerokuAppAPI(connection, connection.executeCommand(new AppCreateCommand(stack.value).withName(appName)).get("name"));
    }


}
