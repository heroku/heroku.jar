package com.heroku.api;


import com.heroku.api.command.*;
import com.heroku.api.connection.Connection;

import java.util.Map;

public class HerokuAppAPI {

    public final Connection connection;
    public final String appName;
    public final CommandConfig baseConfig;

    public HerokuAppAPI(Connection connection, String name) {
        this.connection = connection;
        this.appName = name;
        this.baseConfig = new CommandConfig().onStack(HerokuStack.Cedar).app(appName);
    }

    public HerokuAppAPI create() {
        connection.executeCommand(new AppCreateCommand(baseConfig));
        return this;
    }

    public void destroy() {
        connection.executeCommand(new AppDestroyCommand(baseConfig));
    }

    public Map<String, String> info() {
        return connection.executeCommand(new AppCommand(baseConfig)).getData();
    }

    public HerokuAppAPI addCollaborator(String collaborator) {
        connection.executeCommand(new SharingAddCommand(baseConfig.with(HerokuRequestKey.collaborator, collaborator)));
        return this;
    }

    public HerokuAppAPI removeCollaborator(String collaborator) {
        connection.executeCommand(new SharingRemoveCommand(baseConfig.with(HerokuRequestKey.collaborator, collaborator)));
        return this;
    }

    public void transferApp(String to) {
        connection.executeCommand(new SharingTransferCommand(baseConfig.with(HerokuRequestKey.collaborator, to)));
    }

}
