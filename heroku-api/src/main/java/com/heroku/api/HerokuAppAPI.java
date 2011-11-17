package com.heroku.api;


import com.heroku.api.command.app.AppCreate;
import com.heroku.api.command.app.AppDestroy;
import com.heroku.api.command.app.AppInfo;
import com.heroku.api.command.log.Log;
import com.heroku.api.command.sharing.SharingAdd;
import com.heroku.api.command.sharing.SharingRemove;
import com.heroku.api.command.sharing.SharingTransfer;
import com.heroku.api.connection.Connection;

import java.util.Map;

public class HerokuAppAPI {

    final Connection<?> connection;
    final String appName;

    public HerokuAppAPI(Connection<?> connection, String name) {
        this.connection = connection;
        this.appName = name;
    }

    public Map<String, String> create(HerokuStack stack) {
        return connection.executeCommand(new AppCreate(stack.value).withName(appName)).getData();
    }

    public HerokuAppAPI createAnd(HerokuStack stack) {
        create(stack);
        return this;
    }

    public void destroy() {
        connection.executeCommand(new AppDestroy(appName));
    }

    public Map<String, String> info() {
        return connection.executeCommand(new AppInfo(appName)).getData();
    }

    public HerokuAppAPI addCollaborator(String collaborator) {
        connection.executeCommand(new SharingAdd(appName, collaborator));
        return this;
    }

    public HerokuAppAPI removeCollaborator(String collaborator) {
        connection.executeCommand(new SharingRemove(appName, collaborator));
        return this;
    }

    public void transferApp(String to) {
        connection.executeCommand(new SharingTransfer(appName, to));
    }

    public String getLogChunk() {
        return connection.executeCommand(connection.executeCommand(new Log(appName)).getNextCommand()).getText();
    }

    public HerokuAPI api() {
        return new HerokuAPI(connection);
    }

    public String getAppName() {
        return appName;
    }

    public Connection getConnection() {
        return connection;
    }
}
