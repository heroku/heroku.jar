package com.heroku.api;


import com.heroku.api.connection.Connection;
import com.heroku.api.model.*;
import com.heroku.api.request.addon.AddonInstall;
import com.heroku.api.request.addon.AddonRemove;
import com.heroku.api.request.addon.AppAddonsList;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.app.AppInfo;
import com.heroku.api.request.app.AppRename;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.config.ConfigList;
import com.heroku.api.request.config.ConfigRemove;
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

public class HerokuAppAPI {

    final Connection<?> connection;
    final String appName;

    public HerokuAppAPI(Connection<?> connection, String name) {
        this.connection = connection;
        this.appName = name;
    }

    public HerokuAppAPI rename(String newName) {
        return new HerokuAppAPI(connection, connection.execute(new AppRename(appName, newName)).getName());
    }

    public AddonChange addAddon(String addonName) {
        return connection.execute(new AddonInstall(appName, addonName));
    }

    public List<Addon> listAddons() {
        return connection.execute(new AppAddonsList(appName));
    }
    
    public AddonChange removeAddon(String addonName) {
        return connection.execute(new AddonRemove(appName, addonName));
    }

    /*todo need addon remove*/
    /*todo need addon upgrade/downgrade*/

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
    
    public void scale(String processType, int quantity) {
        connection.execute(new Scale(appName, processType, quantity));
    }

    public List<Proc> listProcesses() {
        return connection.execute(new ProcessList(appName));
    }

    public List<Release> listReleases() {
        return connection.execute(new ListReleases(appName));
    }

    public String rollback(String releaseName) {
        return connection.execute(new Rollback(appName, releaseName));
    }
    
    public Release releaseInfo(String releaseName) {
        return connection.execute(new ReleaseInfo(appName, releaseName));
    }

    public List<Collaborator> listCollaborators() {
        return connection.execute(new CollabList(appName));
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
        return connection.execute(new ConfigList(appName));
    }

    public Map<String, String> removeConfig(String configVarName) {
        return connection.execute(new ConfigRemove(appName, configVarName));
    }

    public void transferApp(String to) {
        connection.execute(new SharingTransfer(appName, to));
    }

    public LogStreamResponse logs() {
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
