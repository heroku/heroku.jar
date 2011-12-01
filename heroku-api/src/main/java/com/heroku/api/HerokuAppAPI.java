package com.heroku.api;


import com.heroku.api.connection.Connection;
import com.heroku.api.model.App;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.app.AppInfo;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.config.ConfigList;
import com.heroku.api.request.config.ConfigRemove;
import com.heroku.api.request.log.Log;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sharing.SharingAdd;
import com.heroku.api.request.sharing.SharingRemove;
import com.heroku.api.request.sharing.SharingTransfer;

import java.util.List;
import java.util.Map;

public class HerokuAppAPI {

    final Connection<?> connection;
    final String appName;

    public HerokuAppAPI(Connection<?> connection, String name) {
        this.connection = connection;
        this.appName = name;
    }

    public App create(Heroku.Stack stack) {
        return connection.execute(new AppCreate(stack).withName(appName));
    }

    public HerokuAppAPI createAnd(Heroku.Stack stack) {
        create(stack);
        return this;
    }

    public void destroy() {
        connection.execute(new AppDestroy(appName));
    }

    public App info() {
        return connection.execute(new AppInfo(appName));
    }

    public List<Map<String, String>> listCollaborators() {
        return connection.execute(new CollabList(appName)).getData();
    }

    public HerokuAppAPI addCollaborator(String collaborator) {
        connection.execute(new SharingAdd(appName, collaborator));
        return this;
    }

    public HerokuAppAPI removeCollaborator(String collaborator) {
        connection.execute(new SharingRemove(appName, collaborator));
        return this;
    }

    public void addConfig(String config) {
        connection.execute(new ConfigAdd(appName, config));
    }

    public Map<String, String> listConfig() {
        return connection.execute(new ConfigList(appName)).getData();
    }

    public Map<String, String> removeConfig(String configVarName) {
        return connection.execute(new ConfigRemove(appName, configVarName)).getData();
    }

    public void transferApp(String to) {
        connection.execute(new SharingTransfer(appName, to));
    }

    public LogStreamResponse getLogChunk() {
        return connection.execute(new Log(appName));
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
