package com.heroku.api.request;

import com.heroku.api.Heroku;
import com.heroku.api.HerokuAPI;
import com.heroku.api.model.*;
import com.heroku.api.request.addon.AddonInstall;
import com.heroku.api.request.addon.AddonList;
import com.heroku.api.request.addon.AppAddonsList;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.app.AppInfo;
import com.heroku.api.request.app.AppList;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.config.ConfigList;
import com.heroku.api.request.config.ConfigRemove;
import com.heroku.api.request.log.Log;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.ps.ProcessList;
import com.heroku.api.request.ps.Restart;
import com.heroku.api.request.ps.Scale;
import com.heroku.api.response.Unit;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sharing.SharingAdd;
import com.heroku.api.request.sharing.SharingRemove;
import com.heroku.api.request.sharing.SharingTransfer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.heroku.api.Heroku.Stack.Cedar;
import static org.testng.Assert.*;


/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class RequestIntegrationTest extends BaseRequestIntegrationTest {

    // test app gets transferred to this user until we have a second user in auth-test.properties
    private static final String DEMO_EMAIL = "jw+demo@heroku.com";

    @Test
    public void testCreateAppCommand() throws IOException {
        AppCreate cmd = new AppCreate("Cedar");
        com.heroku.api.model.App response = connection.execute(cmd);

        assertNotNull(response.getId());
        assertEquals(Heroku.Stack.fromString(response.getStack()), Heroku.Stack.Cedar);
        deleteApp(response.getName());
    }
    
    @DataProvider
    public Object[][] logParameters() {
        return new Object[][] {
                { new Log(createApp().getName()) },
                { new Log(createApp().getName(), true) },
                { Log.logFor(createApp().getName()).tail(false).num(1).getRequest() }
        };
    }
    
    @Test(dataProvider = "logParameters")
    public void testLogCommand(Log log) throws IOException, InterruptedException {
        System.out.println("Sleeping to wait for logplex provisioning");
        Thread.sleep(10000);
        LogStreamResponse logsResponse = connection.execute(log);
        assertLogIsReadable(logsResponse);
    }

    @Test(dataProvider = "app")
    public void testAppCommand(App app) throws IOException {
        AppInfo cmd = new AppInfo(app.getName());
        App response = connection.execute(cmd);
        assertEquals(response.getName(), app.getName());
    }

    @Test(dataProvider = "app")
    public void testListAppsCommand(App app) throws IOException {
        AppList cmd = new AppList();
        List<App> response = connection.execute(cmd);
        assertNotNull(response);
        assertTrue(response.size() > 0, "At least one app should be present, but there are none.");
    }

    // don't use the app dataprovider because it'll try to delete an already deleted app
    @Test
    public void testDestroyAppCommand() throws IOException {
        AppDestroy cmd = new AppDestroy(HerokuAPI.with(connection).newapp(Cedar).getAppName());
        connection.execute(cmd);
    }

    @Test(dataProvider = "app")
    public void testSharingAddCommand(App app) throws IOException {
        SharingAdd cmd = new SharingAdd(app.getName(), DEMO_EMAIL);
        connection.execute(cmd);
    }

    // if we do this then we will no longer be able to remove the app
    // we need two users in auth-test.properties so that we can transfer it to one and still control it,
    // rather than transferring it to a black hole
    @Test(dataProvider = "app")
    public void testSharingTransferCommand(App app) throws IOException {
        Request<Unit> sharingAddReq = new SharingAdd(app.getName(), DEMO_EMAIL);
        connection.execute(sharingAddReq);

        SharingTransfer sharingTransferCommand = new SharingTransfer(app.getName(), DEMO_EMAIL);
        connection.execute(sharingTransferCommand);

    }

    @Test(dataProvider = "app")
    public void testSharingRemoveCommand(App app) throws IOException {
        SharingAdd sharingAddCommand = new SharingAdd(app.getName(), DEMO_EMAIL);
        connection.execute(sharingAddCommand);

        SharingRemove cmd = new SharingRemove(app.getName(), DEMO_EMAIL);
        connection.execute(cmd);

    }

    @Test(dataProvider = "app")
    public void testConfigAddCommand(App app) throws IOException {
        ConfigAdd cmd = new ConfigAdd(app.getName(), "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");
        connection.execute(cmd);
    }

    @Test(dataProvider = "app")
    public void testConfigCommand(App app) {
        addConfig(app, "FOO", "BAR");
        Request<Map<String, String>> req = new ConfigList(app.getName());
        Map<String, String> response = connection.execute(req);
        assertNotNull(response.get("FOO"));
        assertEquals(response.get("FOO"), "BAR");
    }

    @Test(dataProvider = "app")
    public void testConfigRemoveCommand(App app) {
        addConfig(app, "FOO", "BAR", "JOHN", "DOE");
        Request<Map<String, String>> removeRequest = new ConfigRemove(app.getName(), "FOO");
        connection.execute(removeRequest);

        Request<Map<String, String>> listRequest = new ConfigList(app.getName());
        Map<String, String> response = connection.execute(listRequest);

        assertNotNull(response.get("JOHN"), "Config var 'JOHN' should still exist, but it's not there.");
        assertNull(response.get("FOO"));
    }

    @Test(dataProvider = "app")
    public void testProcessCommand(App app) {
        Request<List<Proc>> req = new ProcessList(app.getName());
        List<Proc> response = connection.execute(req);
        assertNotNull(response, "Expected a non-null response for a new app, but the data was null.");
        assertEquals(response.size(), 1);
    }

    @Test(dataProvider = "app")
    public void testScaleCommand(App app) {
        Request<Unit> req = new Scale(app.getName(), "web", 1);
        connection.execute(req);
    }

    @Test(dataProvider = "app")
    public void testRestartCommand(App app) {
        Request<Unit> req = new Restart(app.getName());
        connection.execute(req);
    }

    @Test
    public void testListAddons() {
        AddonList req = new AddonList();
        List<Addon> response = connection.execute(req);
        assertNotNull(response, "Expected a response from listing addons, but the result is null.");
    }

    @Test(dataProvider = "app")
    public void testListAppAddons(App app) {
        Request<List<Addon>> req = new AppAddonsList(app.getName());
        List<Addon> response = connection.execute(req);
        assertNotNull(response);
        assertTrue(response.size() > 0, "Expected at least one addon to be present.");
        assertNotNull(response.get(0).getName());
    }

    @Test(dataProvider = "app")
    public void testAddAddonToApp(App app) {
        AddonInstall req = new AddonInstall(app.getName(), "shared-database:5mb");
        AddonChange response = connection.execute(req);
        assertEquals(response.getStatus(), "Installed");
    }

    @Test(dataProvider = "app")
    public void testCollaboratorList(App app) {
        Request<List<Collaborator>> req = new CollabList(app.getName());
        List<Collaborator> xmlArrayResponse = connection.execute(req);
        assertEquals(xmlArrayResponse.size(), 1);
        assertNotNull(xmlArrayResponse.get(0).getEmail());
    }




}