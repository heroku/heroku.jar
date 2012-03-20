package com.heroku.api;


import com.heroku.api.connection.Connection;
import com.heroku.api.connection.ConnectionFactory;
import com.heroku.api.request.addon.AddonInstall;
import com.heroku.api.request.addon.AddonList;
import com.heroku.api.request.addon.AddonRemove;
import com.heroku.api.request.addon.AppAddonsList;
import com.heroku.api.request.app.*;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.config.ConfigList;
import com.heroku.api.request.config.ConfigRemove;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.log.Log;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.ps.ProcessList;
import com.heroku.api.request.ps.Restart;
import com.heroku.api.request.ps.Scale;
import com.heroku.api.request.releases.ListReleases;
import com.heroku.api.request.releases.ReleaseInfo;
import com.heroku.api.request.releases.Rollback;
import com.heroku.api.request.run.Run;
import com.heroku.api.request.run.RunResponse;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sharing.SharingAdd;
import com.heroku.api.request.sharing.SharingRemove;
import com.heroku.api.request.sharing.SharingTransfer;
import com.heroku.api.request.user.UserInfo;

import java.util.List;
import java.util.Map;

public class HerokuAPI {

    final Connection connection;
    final String apiKey;


    public HerokuAPI(String apiKey) {
        this(ConnectionFactory.get(), apiKey);
    }

    public HerokuAPI(Connection connection, String apiKey) {
        this.connection = connection;
        this.apiKey = apiKey;
    }

    public Connection getConnection() {
        return connection;
    }


    public User getUserInfo() {
        return connection.execute(new UserInfo(), apiKey);
    }

    public void addKey(String sshKey) {
        connection.execute(new KeyAdd(sshKey), apiKey);
    }

    public void removeKey(String sshKey) {
        connection.execute(new KeyRemove(sshKey), apiKey);
    }

    public List<Key> listKeys() {
        return connection.execute(new KeyList(), apiKey);
    }

    public List<App> listApps() {
        return connection.execute(new AppList(), apiKey);
    }

    public App getApp(String name) {
        return connection.execute(new AppInfo(name), apiKey);
    }

    public App createApp() {
        return connection.execute(new AppCreate(new App().on(Heroku.Stack.Cedar)), apiKey);
    }

    public App createApp(App app) {
        return connection.execute(new AppCreate(app), apiKey);
    }

    public String renameApp(String appName, String newName) {
        return connection.execute(new AppRename(appName, newName), apiKey).getName();
    }

    public void destroyApp(String appName) {
        connection.execute(new AppDestroy(appName), apiKey);
    }

    public AddonChange addAddon(String appName, String addonName) {
        return connection.execute(new AddonInstall(appName, addonName), apiKey);
    }

    public List<Addon> listAllAddons() {
        return connection.execute(new AddonList(), apiKey);
    }

    public List<Addon> listAppAddons(String appName) {
        return connection.execute(new AppAddonsList(appName), apiKey);
    }

    public AddonChange removeAddon(String appName, String addonName) {
        return connection.execute(new AddonRemove(appName, addonName), apiKey);
    }

    // TODO: need addon upgrade/downgrade

    public void scaleProcess(String appName, String processType, int quantity) {
        connection.execute(new Scale(appName, processType, quantity), apiKey);
    }

    public List<Proc> listProcesses(String appName) {
        return connection.execute(new ProcessList(appName), apiKey);
    }

    public List<Release> listReleases(String appName) {
        return connection.execute(new ListReleases(appName), apiKey);
    }

    public String rollback(String appName, String releaseName) {
        return connection.execute(new Rollback(appName, releaseName), apiKey);
    }

    public Release getReleaseInfo(String appName, String releaseName) {
        return connection.execute(new ReleaseInfo(appName, releaseName), apiKey);
    }

    public List<Collaborator> listCollaborators(String appName) {
        return connection.execute(new CollabList(appName), apiKey);
    }

    public void addCollaborator(String appName, String collaborator) {
        connection.execute(new SharingAdd(appName, collaborator), apiKey);
    }

    public void removeCollaborator(String appName, String collaborator) {
        connection.execute(new SharingRemove(appName, collaborator), apiKey);
    }

    public void addConfig(String appName, Map<String, String> config) {
        String jsonConfig = "{";
        String separator = "";
        for (Map.Entry<String, String> configEntry : config.entrySet()) {
            jsonConfig = jsonConfig.concat(String.format("%s \"%s\":\"%s\"", separator, configEntry.getKey(), configEntry.getValue()));
            separator = ",";
        }
        jsonConfig = jsonConfig.concat("}");
        connection.execute(new ConfigAdd(appName, jsonConfig), apiKey);
    }

    public Map<String, String> listConfig(String appName) {
        return connection.execute(new ConfigList(appName), apiKey);
    }

    public Map<String, String> removeConfig(String appName, String configVarName) {
        return connection.execute(new ConfigRemove(appName, configVarName), apiKey);
    }

    public void transferApp(String appName, String to) {
        connection.execute(new SharingTransfer(appName, to), apiKey);
    }

    public LogStreamResponse getLogs(String appName) {
        return connection.execute(new Log(appName), apiKey);
    }

    public LogStreamResponse getLogs(Log.LogRequestBuilder logRequest) {
        return connection.execute(new Log(logRequest), apiKey);
    }

    public void run(String appName, String command) {
        connection.execute(new Run(appName, command), apiKey);
    }

    public RunResponse runAttached(String appName, String command) {
        return connection.execute(new Run(appName, command, true), apiKey);
    }

    public void restart(String appName) {
        connection.execute(new Restart(appName), apiKey);
    }

    public void restartProcessByType(String appName, String type) {
        connection.execute(new Restart.ProcessTypeRestart(appName, type), apiKey);
    }

    public void restartProcessByName(String appName, String procName) {
        connection.execute(new Restart.NamedProcessRestart(appName, procName), apiKey);
    }
}
