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
import com.heroku.api.request.ps.Scale;
import com.heroku.api.request.releases.ListReleases;
import com.heroku.api.request.releases.ReleaseInfo;
import com.heroku.api.request.releases.Rollback;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sharing.SharingAdd;
import com.heroku.api.request.sharing.SharingRemove;
import com.heroku.api.request.sharing.SharingTransfer;

import java.util.List;
import java.util.Map;

public class HerokuAPI {

    final Connection<?> connection;
    
    public HerokuAPI(HerokuAPIConfig config) {
        this(ConnectionFactory.get(config));
    }

    public HerokuAPI(Connection<?> connection) {
        this.connection = connection;
    }
    
    public Connection<?> getConnection() {
        return connection;
    }
    
    public String getApiKey() {
        return connection.getApiKey();
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
    
    public List<App> listApps() {
        return connection.execute(new AppList());
    }

    public App getApp(String name) {
        return connection.execute(new AppInfo(name));
    }

    public App createApp() {
        return connection.execute(new AppCreate(new App().on(Heroku.Stack.Cedar)));
    }

    public App createApp(App app) {
        return connection.execute(new AppCreate(app));
    }

    public String renameApp(String appName, String newName) {
        return connection.execute(new AppRename(appName, newName)).getName();
    }

    public void destroyApp(String appName) {
        connection.execute(new AppDestroy(appName));
    }

    public AddonChange addAddon(String appName, String addonName) {
        return connection.execute(new AddonInstall(appName, addonName));
    }

    public List<Addon> listAllAddons() {
        return connection.execute(new AddonList());
    }

    public List<Addon> listAppAddons(String appName) {
        return connection.execute(new AppAddonsList(appName));
    }

    public AddonChange removeAddon(String appName, String addonName) {
        return connection.execute(new AddonRemove(appName, addonName));
    }

    // TODO: need addon upgrade/downgrade

    public void scaleProcess(String appName, String processType, int quantity) {
        connection.execute(new Scale(appName, processType, quantity));
    }

    public List<Proc> listProcesses(String appName) {
        return connection.execute(new ProcessList(appName));
    }

    public List<Release> listReleases(String appName) {
        return connection.execute(new ListReleases(appName));
    }

    public String rollback(String appName, String releaseName) {
        return connection.execute(new Rollback(appName, releaseName));
    }

    public Release getReleaseInfo(String appName, String releaseName) {
        return connection.execute(new ReleaseInfo(appName, releaseName));
    }

    public List<Collaborator> listCollaborators(String appName) {
        return connection.execute(new CollabList(appName));
    }

    public void addCollaborator(String appName, String collaborator) {
        connection.execute(new SharingAdd(appName, collaborator));
    }

    public void removeCollaborator(String appName, String collaborator) {
        connection.execute(new SharingRemove(appName, collaborator));
    }

    public void addConfig(String appName, String config) {
        connection.execute(new ConfigAdd(appName, config));
    }

    public Map<String, String> listConfig(String appName) {
        return connection.execute(new ConfigList(appName));
    }

    public Map<String, String> removeConfig(String appName, String configVarName) {
        return connection.execute(new ConfigRemove(appName, configVarName));
    }

    public void transferApp(String appName, String to) {
        connection.execute(new SharingTransfer(appName, to));
    }

    public LogStreamResponse getLogs(String appName) {
        return connection.execute(new Log(appName));
    }

}
