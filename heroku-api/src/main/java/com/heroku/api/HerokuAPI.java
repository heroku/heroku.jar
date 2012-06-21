package com.heroku.api;


import com.heroku.api.connection.Connection;
import com.heroku.api.connection.ConnectionFactory;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.app.AppClone;
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
import com.heroku.api.request.login.BasicAuthLogin;
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
import com.heroku.api.request.stack.StackList;
import com.heroku.api.request.stack.StackMigrate;
import com.heroku.api.request.user.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * A convenience class for making HTTP requests to the Heroku API for a given user. An underlying {@link Connection} is created
 * for each instance of HerokuAPI. To make HTTP requests to the Heroku API in multi-user or systems that have resource
 * constraints (e.g. a pool of <code>Connection</code> objects are required), <code>Connection</code> should be used
 * directly.
 * <p/>
 * Example usage:
 * <pre>{@code
 *     HerokuAPI heroku = new HerokuAPI("apiKey");
 *     App app = heroku.createApp();
 *     heroku.scaleProcess(app.getName(), "web", 1)
 * }
 * </pre>
 * 
 * {@link RuntimeException} will be thrown for any request failures.
 */
public class HerokuAPI {

    protected final Connection connection;
    protected final String apiKey;

    /**
     * Logs into the Heroku API and retrieves an API key for a given username and password using HTTP Basic Authentication.
     * @param username Heroku username.
     * @param password Heroku password.
     * @return An API key that can be used for subsequent API calls.
     */
    public static String obtainApiKey(String username, String password) {
        Connection tmpConn = ConnectionFactory.get();
        LoginVerification verification = tmpConn.execute(new BasicAuthLogin(username, password), null);
        return verification.getApiKey();
    }

    /**
     * Constructs a HerokuAPI with a {@link Connection} based on the first {@link com.heroku.api.connection.ConnectionProvider}
     * found on the classpath.
     * @param apiKey User's API key found at https://api.heroku.com/account
     */
    public HerokuAPI(String apiKey) {
        this(ConnectionFactory.get(), apiKey);
    }

    /**
     * @param connection
     * @param apiKey User's API key found at https://api.heroku.com/account
     */
    public HerokuAPI(Connection connection, String apiKey) {
        this.connection = connection;
        this.apiKey = apiKey;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Information for the current user.
     * @return
     */
    public User getUserInfo() {
        return connection.execute(new UserInfo(), apiKey);
    }

    /**
     * Add an ssh key to the current user's account.
     * @param sshKey Public key value. e.g. ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCz29znMi/UJX/nvkRSO5FFugKhU9DkkI53E0vXUnP8zeLFxMgyUqmXryPVjWtGzz2LRWqjm14SbqHAmM44pGHVfBIp6wCKBWSUYGv/FxOulwYgtWzz4moxWLZrFyWWgJAnehcVUifHNgzKwT2ovWm2ns52681Z8yFK3K8/uLStDjLIaPePEOaxaTvgIxZNsfyEoXoHcyTPwdR1GtQuDTuDYqYmjmPCoKybYnXrTQ1QFuQxDneBkswQYSl0H2aLf3uBK4F01hr+azXQuSe39eSV4I/TqzmNJlanpILT9Jz3/J1i4r6brpF3AxLnFnb9ufIbzQAIa/VZIulfrZkcBsUl user@company.com
     */
    public void addKey(String sshKey) {
        connection.execute(new KeyAdd(sshKey), apiKey);
    }

    /**
     * Delete an ssh key from the current user's account.
     * @param sshKey The comment in the public key. See {@link #listKeys} to get a list of keys that can be removed.
     */
    public void removeKey(String sshKey) {
        connection.execute(new KeyRemove(sshKey), apiKey);
    }

    /**
     * Get a list of keys associated with the current user's account.
     * @return
     */
    public List<Key> listKeys() {
        return connection.execute(new KeyList(), apiKey);
    }

    /**
     * List all apps for the current user's account.
     * @return
     */
    public List<App> listApps() {
        return connection.execute(new AppList(), apiKey);
    }

    /**
     * Get information about a specific app.
     * @param name The name of the app. See {@link #listApps} to get a list of apps and their names.
     * @return
     */
    public App getApp(String name) {
        return connection.execute(new AppInfo(name), apiKey);
    }

    /**
     * Checks if an app with the given name exists on Heroku.
     *
     * A true response does not necessarily indicate the user has access to the app.
     * Use {@link #getApp(String)} to check user access.
     *
     * @param name name of the app
     * @return true if an app exists with the specified name
     */
    public boolean appExists(String name) {
        return connection.execute(new AppExists(name), apiKey);
    }

    /**
     * Checks if the given app name is available on Heroku.
     *
     * @param name name of the app
     * @return true if the app name is available
     */
    public boolean isAppNameAvailable(String name) {
        return !appExists(name);
    }

    /**
     * Create a new app on the {@link Heroku.Stack.Cedar} stack. For more information about the Cedar stack, please see
     * the <a href="http://devcenter.heroku.com">Dev Center</a>.
     * @return
     */
    public App createApp() {
        return connection.execute(new AppCreate(new App().on(Heroku.Stack.Cedar)), apiKey);
    }

    /**
     * Create a new app using {@link App} to specify parameters. {@link App} has convenience methods for specifying
     * parameters to send as part of the request. Typically, these will be the name of the app and the requested stack. e.g.
     * <pre>{@code heroku.createApp(new App().on(Heroku.Stack.Cedar).named("new-app")}</pre>
     * @param app See {@link App}
     * @return
     */
    public App createApp(App app) {
        return connection.execute(new AppCreate(app), apiKey);
    }

    /**
     * Clone an existing app that has previously been designated as a template
     * into the authenticated user's account with a randomly generated name.
     * App cloning is only supported on the {@link Heroku.Stack.Cedar} stack.
     *
     * @param templateAppName Name of the template app to clone.
     * @return details about the cloned app
     */
    public App cloneApp(String templateAppName) {
        return connection.execute(new AppClone(templateAppName, new App()), apiKey);
    }

    /**
     * Clone an existing app that has previously been designated as a template
     * into the authenticated user's account with details specified in the target app.
     * Currently, only specifying the name of the target app is supported.
     * App cloning is only supported on the {@link Heroku.Stack.Cedar} stack.
     *
     * @param templateAppName Name of the template app to clone.
     * @param targetApp Details about the target app.
     * @return details about the cloned targetApp
     */
    public App cloneApp(String templateAppName, App targetApp) {
        return connection.execute(new AppClone(templateAppName, targetApp), apiKey);
    }
    
    /**
     * Rename an existing app.
     * @param appName Existing app name. See {@link #listApps()} for names that can be used.
     * @param newName New name to give the existing app.
     * @return
     */
    public String renameApp(String appName, String newName) {
        return connection.execute(new AppRename(appName, newName), apiKey).getName();
    }

    /**
     * Delete an app.
     * @param appName
     */
    public void destroyApp(String appName) {
        connection.execute(new AppDestroy(appName), apiKey);
    }

    /**
     * Add an addon to the app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param addonName Addon name. See {@link #listAllAddons} to get a list of addons that can be used.
     * @return
     */
    public AddonChange addAddon(String appName, String addonName) {
        return connection.execute(new AddonInstall(appName, addonName), apiKey);
    }

    /**
     * Get a list of all addons available. Refer to http://addons.heroku.com for more information about addons.
     * @return
     */
    public List<Addon> listAllAddons() {
        return connection.execute(new AddonList(), apiKey);
    }

    /**
     * List the addons already added to an app.
     * @param appName
     * @return
     */
    public List<Addon> listAppAddons(String appName) {
        return connection.execute(new AppAddonsList(appName), apiKey);
    }

    /**
     * Remove an addon from an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param addonName Addon name. See {@link #listAppAddons} for a list of addons that can be used.
     * @return
     */
    public AddonChange removeAddon(String appName, String addonName) {
        return connection.execute(new AddonRemove(appName, addonName), apiKey);
    }

    // TODO: need addon upgrade/downgrade

    /**
     * Change the number of processes running for a given process type.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param processType Process name. See {@link #listProcesses} for a list of processes that can be used.
     * @param quantity The number to scale the process to.
     */
    public void scaleProcess(String appName, String processType, int quantity) {
        connection.execute(new Scale(appName, processType, quantity), apiKey);
    }

    /**
     * List of processes running for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return
     */
    public List<Proc> listProcesses(String appName) {
        return connection.execute(new ProcessList(appName), apiKey);
    }

    /**
     * List of releases for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return
     */
    public List<Release> listReleases(String appName) {
        return connection.execute(new ListReleases(appName), apiKey);
    }

    /**
     * Rollback an app to a specific release.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param releaseName Release name. See {@link #listReleases} for a list of the app's releases.
     * @return
     */
    public String rollback(String appName, String releaseName) {
        return connection.execute(new Rollback(appName, releaseName), apiKey);
    }

    /**
     * Information about a specific release.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param releaseName Release name. See {@link #listReleases} for a list of the app's releases.
     * @return
     */
    public Release getReleaseInfo(String appName, String releaseName) {
        return connection.execute(new ReleaseInfo(appName, releaseName), apiKey);
    }

    /**
     * Get a list of collaborators that are allowed access to an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return
     */
    public List<Collaborator> listCollaborators(String appName) {
        return connection.execute(new CollabList(appName), apiKey);
    }

    /**
     * Add a collaborator to an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param collaborator Username of the collaborator to add. This is usually in the form of "user@company.com".
     */
    public void addCollaborator(String appName, String collaborator) {
        connection.execute(new SharingAdd(appName, collaborator), apiKey);
    }

    /**
     * Remove a collaborator from an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param collaborator See {@link #listCollaborators} for collaborators that can be removed from the app.
     */
    public void removeCollaborator(String appName, String collaborator) {
        connection.execute(new SharingRemove(appName, collaborator), apiKey);
    }

    /**
     * Add environment variables to an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param config Key/Value pairs of environment variables.
     */
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

    /**
     * List all the environment variables for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return
     */
    public Map<String, String> listConfig(String appName) {
        return connection.execute(new ConfigList(appName), apiKey);
    }

    /**
     * Remove an environment variable from an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param configVarName Name of the environment variable. See {@link #listConfig} for variables that can be removed
     * @return
     */
    public Map<String, String> removeConfig(String appName, String configVarName) {
        return connection.execute(new ConfigRemove(appName, configVarName), apiKey);
    }

    /**
     * Transfer the ownership of an application to another user.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param to Username of the person to transfer the app to. This is usually in the form of "user@company.com".
     */
    public void transferApp(String appName, String to) {
        connection.execute(new SharingTransfer(appName, to), apiKey);
    }

    /**
     * Get logs for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return
     */
    public LogStreamResponse getLogs(String appName) {
        return connection.execute(new Log(appName), apiKey);
    }

    /**
     * Get logs for an app by specifying additional parameters.
     * @param logRequest See {@link LogRequestBuilder}
     * @return
     */
    public LogStreamResponse getLogs(Log.LogRequestBuilder logRequest) {
        return connection.execute(new Log(logRequest), apiKey);
    }

    /**
     * Run a one-off process on a Heroku dyno.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param command Bash command to run inside a dyno. See <a href="http://devcenter.heroku.com/articles/oneoff-admin-ps">One-off processes</a>.
     */
    public void run(String appName, String command) {
        connection.execute(new Run(appName, command), apiKey);
    }

    /**
     * Run a one-off process on a Heroku dyno in an attached state. Running in an attached state allows for interactive input. Refer to {@link com.heroku.api.request.run.RunResponse#attach()}
     * for more information on running an attached process.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param command Bash command to run inside a dyno. See <a href="http://devcenter.heroku.com/articles/oneoff-admin-ps">One-off processes</a>.
     * @return
     */
    public RunResponse runAttached(String appName, String command) {
        return connection.execute(new Run(appName, command, true), apiKey);
    }

    /**
     * Restart an app.
     * @param appName See {@link #listApps} for a list of apps that can be used.
     */
    public void restart(String appName) {
        connection.execute(new Restart(appName), apiKey);
    }

    /**
     * Restart a process for an app.
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param type The type of process to restart. e.g. web, worker, etc...
     */
    public void restartProcessByType(String appName, String type) {
        connection.execute(new Restart.ProcessTypeRestart(appName, type), apiKey);
    }

    /**
     * Restart a named process.
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param procName Name of process to restart.
     */
    public void restartProcessByName(String appName, String procName) {
        connection.execute(new Restart.NamedProcessRestart(appName, procName), apiKey);
    }

    /**
     * Migrates an app from its current stack to the specified stack. Stacks must be compatible with one another. e.g. an app can be migrated from
     * {@link com.heroku.api.Heroku.Stack.Bamboo187} to {@link com.heroku.api.Heroku.Stack.Bamboo192}, but not to {@link com.heroku.api.Heroku.Stack.Cedar}.
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param migrateTo Stack to migrate the app to.
     * @return A message about the migration.
     */
    public String migrateStack(String appName, Heroku.Stack migrateTo) {
        return connection.execute(new StackMigrate(appName, migrateTo), apiKey);
    }

    /**
     * Gets a list of stacks available.
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @return List of stacks available.
     */
    public List<StackInfo> listAppStacks(String appName) {
        return connection.execute(new StackList(appName), apiKey);
    }
}
