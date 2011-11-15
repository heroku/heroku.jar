package com.heroku.api.command;

import com.heroku.api.command.addon.AddonList;
import com.heroku.api.command.addon.AddonInstall;
import com.heroku.api.command.addon.AppAddonsList;
import com.heroku.api.command.app.*;
import com.heroku.api.command.config.ConfigAdd;
import com.heroku.api.command.config.ConfigList;
import com.heroku.api.command.config.ConfigRemove;
import com.heroku.api.command.log.LogStream;
import com.heroku.api.command.response.LogStreamResponse;
import com.heroku.api.command.log.Log;
import com.heroku.api.command.response.*;
import com.heroku.api.command.ps.ProcessList;
import com.heroku.api.command.ps.Restart;
import com.heroku.api.command.ps.Scale;
import com.heroku.api.command.sharing.SharingAddCommand;
import com.heroku.api.command.sharing.SharingRemoveCommand;
import com.heroku.api.command.sharing.SharingTransferCommand;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.*;


/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CommandIntegrationTest extends BaseCommandIntegrationTest {

    // test app gets transferred to this user until we have a second user in auth-test.properties
    private static final String DEMO_EMAIL = "jw+demo@heroku.com";

    @Test
    public void testCreateAppCommand() throws IOException {
        Command cmd = new AppCreate("Cedar");
        CommandResponse response = connection.executeCommand(cmd);

        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
    }

    @Test(dataProvider = "app")
    public void testLogCommand(JsonMapResponse app) throws IOException, InterruptedException {
        System.out.println("Sleeping to wait for logplex provisioning");
        Thread.sleep(10000);
        Log logs = new Log(app.get("name"));
        LogsResponse logsResponse = connection.executeCommand(logs);
        String logChunk = connection.executeCommand(logsResponse.getData()).getData();
        assertTrue(logChunk.length() > 0, "No Logs Returned");
    }

    @Test(dataProvider = "app")
    public void testLogStreamCommand(JsonMapResponse app) throws IOException, InterruptedException {
        System.out.println("Sleeping to wait for logplex provisioning");
        Thread.sleep(10000);
        LogStream logs = new LogStream(app.get("name"));
        LogStreamResponse logsResponse = connection.executeCommand(logs);
        InputStream in = connection.executeCommand(logsResponse.getData()).getData();
        byte[] read = new byte[1024];
        assertTrue(in.read(read) > -1, "No Logs Returned");
    }

    @Test(dataProvider = "app")
    public void testAppCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppInfo(app.get("name"));
        CommandResponse response = connection.executeCommand(cmd);
        assertEquals(response.get("name"), app.get("name"));
    }

    @Test(dataProvider = "app")
    public void testListAppsCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppList();
        CommandResponse response = connection.executeCommand(cmd);
        assertNotNull(response.get(app.get("name")));
    }

    @Test(dataProvider = "app")
    public void testDestroyAppCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppDestroy(app.get("name"));
        CommandResponse response = connection.executeCommand(cmd);
    }

    @Test(dataProvider = "app")
    public void testSharingAddCommand(JsonMapResponse app) throws IOException {
        Command cmd = new SharingAddCommand(app.get("name"), DEMO_EMAIL);
        CommandResponse response = connection.executeCommand(cmd);
    }

    // if we do this then we will no longer be able to remove the app
    // we need two users in auth-test.properties so that we can transfer it to one and still control it,
    // rather than transferring it to a black hole
    @Test(dataProvider = "app")
    public void testSharingTransferCommand(JsonMapResponse app) throws IOException {
        Command sharingAddCommand = new SharingAddCommand(app.get("name"), DEMO_EMAIL);
        connection.executeCommand(sharingAddCommand);

        Command sharingTransferCommand = new SharingTransferCommand(app.get("name"), DEMO_EMAIL);
        CommandResponse sharingTransferCommandResponse = connection.executeCommand(sharingTransferCommand);

    }

    @Test(dataProvider = "app")
    public void testSharingRemoveCommand(JsonMapResponse app) throws IOException {
        Command sharingAddCommand = new SharingAddCommand(app.get("name"), DEMO_EMAIL);
        connection.executeCommand(sharingAddCommand);

        Command cmd = new SharingRemoveCommand(app.get("name"), DEMO_EMAIL);
        CommandResponse response = connection.executeCommand(cmd);

    }

    @Test(dataProvider = "app")
    public void testConfigAddCommand(JsonMapResponse app) throws IOException {
        Command cmd = new ConfigAdd(app.get("name"), "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");
        CommandResponse response = connection.executeCommand(cmd);
    }

    @Test(dataProvider = "app")
    public void testConfigCommand(JsonMapResponse app) {
        addConfig(app, "FOO", "BAR");
        Command<JsonMapResponse> cmd = new ConfigList(app.get("name"));
        JsonMapResponse response = connection.executeCommand(cmd);
        assertNotNull(response.get("FOO"));
        assertEquals(response.get("FOO"), "BAR");
    }

    @Test(dataProvider = "app",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "FOO is not present.")
    public void testConfigRemoveCommand(JsonMapResponse app) {
        addConfig(app, "FOO", "BAR", "JOHN", "DOE");
        Command<JsonMapResponse> removeCommand = new ConfigRemove(app.get("name"), "FOO");
        connection.executeCommand(removeCommand);

        Command<JsonMapResponse> listCommand = new ConfigList(app.get("name"));
        JsonMapResponse response = connection.executeCommand(listCommand);

        assertNotNull(response.get("JOHN"), "Config var 'JOHN' should still exist, but it's not there.");
        response.get("FOO");
    }

    @Test(dataProvider = "app")
    public void testProcessCommand(JsonMapResponse app) {
        Command<JsonArrayResponse> cmd = new ProcessList(app.get("name"));
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response.getData(), "Expected a non-null response for a new app, but the data was null.");
        assertEquals(response.getData().size(), 1);
    }

    @Test(dataProvider = "app")
    public void testScaleCommand(JsonMapResponse app) {
        Command<EmptyResponse> cmd = new Scale(app.get("name"), "web", 1);
        EmptyResponse response = connection.executeCommand(cmd);
    }
    
    @Test(dataProvider = "app")
    public void testRestartCommand(JsonMapResponse app) {
        Command<EmptyResponse> cmd = new Restart(app.get("name"));
        EmptyResponse response = connection.executeCommand(cmd);
    }
    
    @Test
    public void testListAddons() {
        Command<JsonArrayResponse> cmd = new AddonList();
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response, "Expected a response from listing addons, but the result is null.");
    }
    
    @Test(dataProvider = "app")
    public void testListAppAddons(JsonMapResponse app) {
        Command<JsonArrayResponse> cmd = new AppAddonsList(app.get("name"));
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response);
        assertTrue(response.getData().size() > 0, "Expected at least one addon to be present.");
        assertNotNull(response.get("logging:basic"));
    }

    @Test(dataProvider = "app")
    public void testAddAddonToApp(JsonMapResponse app) {
        Command<JsonMapResponse> cmd = new AddonInstall(app.get("name"), "shared-database:5mb");
        JsonMapResponse response = connection.executeCommand(cmd);
        assertEquals(response.get("status"), "Installed");
    }
}