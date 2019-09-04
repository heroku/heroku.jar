package com.heroku.api;


import java.util.List;
import java.util.Map;

import com.heroku.api.connection.Connection;
import com.heroku.api.connection.ConnectionFactory;
import com.heroku.api.request.addon.AddonInstall;
import com.heroku.api.request.addon.AddonList;
import com.heroku.api.request.addon.AddonRemove;
import com.heroku.api.request.addon.AppAddonsList;
import com.heroku.api.request.app.*;
import com.heroku.api.request.buildpacks.BuildpackInstallationList;
import com.heroku.api.request.buildpacks.BuildpackInstallationUpdate;
import com.heroku.api.request.builds.BuildCreate;
import com.heroku.api.request.builds.BuildInfo;
import com.heroku.api.request.config.ConfigList;
import com.heroku.api.request.config.ConfigUpdate;
import com.heroku.api.request.dynos.DynoList;
import com.heroku.api.request.dynos.DynoRestart;
import com.heroku.api.request.dynos.DynoRestartAll;
import com.heroku.api.request.formation.FormationList;
import com.heroku.api.request.formation.FormationUpdate;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.log.Log;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.releases.ReleaseInfo;
import com.heroku.api.request.releases.ReleaseList;
import com.heroku.api.request.releases.Rollback;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sharing.SharingAdd;
import com.heroku.api.request.sharing.SharingRemove;
import com.heroku.api.request.sharing.SharingTransfer;
import com.heroku.api.request.slugs.SlugCreate;
import com.heroku.api.request.slugs.SlugInfo;
import com.heroku.api.request.sources.SourceCreate;
import com.heroku.api.request.stack.StackList;
import com.heroku.api.request.team.*;
import com.heroku.api.request.user.UserInfo;
import com.heroku.api.util.Range;

/**
 * <p>
 * A convenience class for making HTTP requests to the Heroku API for a given user. An underlying {@link Connection} is created
 * for each instance of HerokuAPI. To make HTTP requests to the Heroku API in multi-user or systems that have resource
 * constraints (e.g. a pool of <code>Connection</code> objects are required), <code>Connection</code> should be used
 * directly.
 * </p>
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
     * Constructs a HerokuAPI with a {@link Connection} based on the first {@link com.heroku.api.connection.ConnectionProvider}
     * found on the classpath.
     * @param apiKey User's API key found at https://api.heroku.com/account
     */
    public HerokuAPI(String apiKey) {
        this(ConnectionFactory.get(), apiKey);
    }

    /**
     * @param connection the connection to Heroku API
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
     * @return user info object
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
     * @return a list of keys
     */
    public List<Key> listKeys() {
        return connection.execute(new KeyList(), apiKey);
    }

    /**
     * List all apps for the current user's account.
     * @return a list of apps
     */
    public Range<App> listApps() {
        return connection.execute(new AppList(), apiKey);
    }

    /**
     * List all apps for the current user's account.
     * @param range The range of apps provided by {@link Range#getNextRange()}
     * @return a list of apps
     */
    public Range<App> listApps(String range) {
        return connection.execute(new AppList(range), apiKey);
    }

    /**
     * Get information about a specific app.
     * @param name The name of the app. See {@link #listApps} to get a list of apps and their names.
     * @return an app object
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
     * Create a new app on the {Heroku.Stack.Heroku18} stack. For more information about the Cedar stack, please see
     * the <a href="http://devcenter.heroku.com">Dev Center</a>.
     * @return an app object
     */
    public App createApp() {
        return connection.execute(new AppCreate(new App().on(Heroku.Stack.Heroku18)), apiKey);
    }

    /**
     * Create a new app using {@link App} to specify parameters. {@link App} has convenience methods for specifying
     * parameters to send as part of the request. Typically, these will be the name of the app and the requested stack. e.g.
     * <pre>{@code heroku.createApp(new App().on(Heroku.Stack.Cedar).named("new-app")}</pre>
     * @param app See {@link App}
     * @return an app object
     */
    public App createApp(App app) {
        return connection.execute(new AppCreate(app), apiKey);
    }

    /**
     * Rename an existing app.
     * @param appName Existing app name. See {@link #listApps()} for names that can be used.
     * @param newName New name to give the existing app.
     * @return the new name of the object
     */
    public String renameApp(String appName, String newName) {
        return connection.execute(new AppRename(appName, newName), apiKey).getName();
    }

    /**
     * Delete an app.
     * @param appName the app name
     */
    public void destroyApp(String appName) {
        connection.execute(new AppDestroy(appName), apiKey);
    }

    /**
     * Add an addon to the app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param addonName Addon name. See {@link #listAllAddons} to get a list of addons that can be used.
     * @return The request object
     */
    public AddonChange addAddon(String appName, String addonName) {
        return connection.execute(new AddonInstall(appName, addonName), apiKey);
    }

    /**
     * Add an addon to the app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param addonName Addon name. See {@link #listAllAddons} to get a list of addons that can be used.
     * @param addonConfig Addon configuration.
     * @return The request object
     */
    public AddonChange addAddon(String appName, String addonName, Map<String,String> addonConfig) {
        return connection.execute(new AddonInstall(appName, addonName, addonConfig), apiKey);
    }

    /**
     * Get a list of all addons available. Refer to http://addons.heroku.com for more information about addons.
     * @return a list of add-ons
     */
    public List<Addon> listAllAddons() {
        return connection.execute(new AddonList(), apiKey);
    }

    /**
     * List the addons already added to an app.
     * @param appName new of the app
     * @return a list of add-ons
     */
    public List<Addon> listAppAddons(String appName) {
        return connection.execute(new AppAddonsList(appName), apiKey);
    }

    /**
     * Remove an addon from an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param addonName Addon name. See {@link #listAppAddons} for a list of addons that can be used.
     * @return the request object
     */
    public AddonChange removeAddon(String appName, String addonName) {
        return connection.execute(new AddonRemove(appName, addonName), apiKey);
    }

    // TODO: need addon upgrade/downgrade

    /**
     * List of releases for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return a list of releases
     */
    public List<Release> listReleases(String appName) {
        return connection.execute(new ReleaseList(appName), apiKey);
    }

    /**
     * List of releases for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param range The range of releases provided by {@link Range#getNextRange()}
     * @return a list of releases
     */
    public List<Release> listReleases(String appName, String range) {
        return connection.execute(new ReleaseList(appName, range), apiKey);
    }

    /**
     * Rollback an app to a specific release.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param releaseUuid Release UUID. See {@link #listReleases} for a list of the app's releases.
     * @return the release object
     */
    public Release rollback(String appName, String releaseUuid) {
        return connection.execute(new Rollback(appName, releaseUuid), apiKey);
    }

    /**
     * Information about a specific release.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param releaseName Release name. See {@link #listReleases} for a list of the app's releases.
     * @return the release object
     */
    public Release getReleaseInfo(String appName, String releaseName) {
        return connection.execute(new ReleaseInfo(appName, releaseName), apiKey);
    }

    /**
     * Get a list of collaborators that are allowed access to an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return list of collaborators
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
     * Update environment variables to an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @param config Key/Value pairs of environment variables.
     */
    public void updateConfig(String appName, Map<String, String> config) {
        connection.execute(new ConfigUpdate(appName, config), apiKey);
    }

    /**
     * List all the environment variables for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return map of config vars
     */
    public Map<String, String> listConfig(String appName) {
        return connection.execute(new ConfigList(appName), apiKey);
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
     * @return log stream response
     */
    public LogStreamResponse getLogs(String appName) {
        return connection.execute(new Log(appName), apiKey);
    }

    /**
     * Get logs for an app.
     * @param appName App name. See {@link #listApps} for a list of apps that can be used.
     * @return log stream response
     */
    public LogStreamResponse getLogs(String appName, Boolean tail) {
        return connection.execute(new Log(appName, tail), apiKey);
    }

    /**
     * Get logs for an app by specifying additional parameters.
     * @param logRequest See {LogRequestBuilder}
     * @return log stream response
     */
    public LogStreamResponse getLogs(Log.LogRequestBuilder logRequest) {
        return connection.execute(new Log(logRequest), apiKey);
    }

    /**
     * Gets a list of stacks available.
     * @return List of stacks available.
     */
    public List<StackInfo> listAppStacks() {
        return connection.execute(new StackList(), apiKey);
    }

    /**
     * Checks if maintenance mode is enabled for the given app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @return true if maintenance mode is enabled
     */
    public boolean isMaintenanceModeEnabled(String appName) {
        App app = connection.execute(new AppInfo(appName), apiKey);
        return app.isMaintenance();
    }

    /**
     * Sets maintenance mode for the given app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param enable true to enable; false to disable
     */
    public void setMaintenanceMode(String appName, boolean enable) {
        connection.execute(new AppUpdate(appName, enable), apiKey);
    }

    /**
     * Gets the slug info for an existing slug
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param processTypes hash mapping process type names to their respective command
     */
    public Slug createSlug(String appName, Map<String, String> processTypes) {
        return connection.execute(new SlugCreate(appName, processTypes), apiKey);
    }

    /**
     * Gets the slug info for an existing slug
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param slugId the unique identifier of the slug
     */
    public Slug getSlugInfo(String appName, String slugId) {
        return connection.execute(new SlugInfo(appName, slugId), apiKey);
    }

    /**
     * Creates a source
     *
     */
    public Source createSource() {
        return connection.execute(new SourceCreate(), apiKey);
    }

    /**
     * Creates a build
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param build the build information
     */
    public Build createBuild(String appName, Build build) {
        return connection.execute(new BuildCreate(appName, build), apiKey);
    }

    /**
     * Gets the info for a running build
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param buildId the unique identifier of the build
     */
    public Build getBuildInfo(String appName, String buildId) {
        return connection.execute(new BuildInfo(appName, buildId), apiKey);
    }

    /**
     * List app dynos for an app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     */
    public Range<Dyno> listDynos(String appName) {
        return connection.execute(new DynoList(appName), apiKey);
    }

    /**
     * Restarts a single dyno
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param dynoId the unique identifier of the dyno to restart
     */
    public void restartDyno(String appName, String dynoId) {
        connection.execute(new DynoRestart(appName, dynoId), apiKey);
    }

    /**
     * Restarts all dynos for an app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     */
    public void restartDynos(String appName) {
        connection.execute(new DynoRestartAll(appName), apiKey);
    }

    /**
     * Scales a process type
     * 
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param processType type of process to maintain
     * @param quantity number of processes to maintain
     */
    public Formation scale(String appName, String processType, int quantity) {
        return connection.execute(new FormationUpdate(appName, processType, quantity), apiKey);
    }

    /**
     * Lists the formation info for an app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     */
    public List<Formation> listFormation(String appName) {
        return connection.execute(new FormationList(appName), apiKey);
    }

    /**
     * Lists the buildpacks installed on an app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     */
    public List<BuildpackInstallation> listBuildpackInstallations(String appName) {
        return connection.execute(new BuildpackInstallationList(appName), apiKey);
    }

    /**
     * Update the list of buildpacks installed on an app
     *
     * @param appName See {@link #listApps} for a list of apps that can be used.
     * @param buildpacks the new list of buildpack names or URLs.
     */
    public void updateBuildpackInstallations(String appName, List<String> buildpacks) {
        connection.execute(new BuildpackInstallationUpdate(appName, buildpacks), apiKey);
    }

    /**
     * Get information about a specific team's app.
     * @param name The name of the app. See {@link #listApps} to get a list of apps and their names.
     * @return an app object
     */
    public TeamApp getTeamApp(String name) {
        return connection.execute(new TeamAppInfo(name), apiKey);
    }

    /**
     * Create a new team
     * @param name The name of the team
     * @return an team object
     */
    public Team createTeam(String name) {
        return connection.execute(new TeamCreate(new Team(name)), apiKey);
    }

    /**
     * Create a new team
     * @param team The name of the team to create this app for
     * @return an team object
     */
    public TeamApp createTeamApp(String team) {
        return connection.execute(new TeamAppCreate(new TeamApp().withTeam(new Team(team)).on(Heroku.Stack.Heroku16)), apiKey);
    }

    /**
     * Create a new team
     * @param app The team app
     * @return an team object
     */
    public TeamApp createTeamApp(TeamApp app) {
        return connection.execute(new TeamAppCreate(app), apiKey);
    }

    /**
     * List all apps for a team.
     * @param team The name or id of the team.
     * @return a list of apps
     */
    public Range<TeamApp> listTeamApps(String team) {
        return connection.execute(new TeamAppList(team), apiKey);
    }

    /**
     * List all apps for a team.
     * @param team The name or id of the team.
     * @param range The range of apps provided by {@link Range#getNextRange()}
     * @return a list of apps
     */
    public Range<TeamApp> listTeamApps(String team, String range) {
        return connection.execute(new TeamAppList(team, range), apiKey);
    }

    /**
     * List all apps for a team.
     * @param team The name or id of the team.
     * @return a list of apps
     */
    public Range<Invoice> listTeamInvoices(String team) {
        return connection.execute(new TeamInvoiceList(team), apiKey);
    }

    /**
     * List all apps for a team.
     * @param team The name or id of the team.
     * @return a list of apps
     */
    public Range<Invoice> listTeamInvoices(String team, String range) {
        return connection.execute(new TeamInvoiceList(team, range), apiKey);
    }
}
