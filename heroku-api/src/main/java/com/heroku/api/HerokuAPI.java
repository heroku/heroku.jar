package com.heroku.api;


import com.heroku.api.command.AppCreateCommand;
import com.heroku.api.command.KeysAddCommand;
import com.heroku.api.command.KeysRemoveCommand;
import com.heroku.api.connection.Connection;
import com.heroku.api.connection.FutureWrapper;

public class HerokuAPI {

    Connection<? extends FutureWrapper> connection;

    public HerokuAPI(Connection<? extends FutureWrapper> connection) {
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
