package com.heroku.api;


import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.KeysAddCommand;
import com.heroku.api.command.KeysRemoveCommand;
import com.heroku.api.command.SharingAddCommand;
import com.heroku.api.connection.Connection;

public class HerokuAPI {

    Connection connection;

    public HerokuAPI(Connection connection) {
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


}
