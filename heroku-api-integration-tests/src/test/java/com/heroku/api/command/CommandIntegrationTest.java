package com.heroku.api.command;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
        Command cmd = new AppCreateCommand("Cedar");
        CommandResponse response = connection.executeCommand(cmd);

        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
    }

    @Test(dataProvider = "app")
    public void testLogCommand(JsonMapResponse app) throws IOException, InterruptedException {
        System.out.println("Sleeping to wait for logplex provisioning");
        Thread.sleep(10000);
        LogsCommand logs = new LogsCommand(app.get("name"));
        LogsResponse logsResponse = connection.executeCommand(logs);
        String logChunk = connection.executeCommand(logsResponse.getData()).getData();
        assertTrue(logChunk.length() > 0, "No Logs Returned");
    }

    @Test(dataProvider = "app")
    public void testLogStreamCommand(JsonMapResponse app) throws IOException, InterruptedException {
        System.out.println("Sleeping to wait for logplex provisioning");
        Thread.sleep(10000);
        LogStreamCommand logs = new LogStreamCommand(app.get("name"));
        LogStreamResponse logsResponse = connection.executeCommand(logs);
        InputStream in = connection.executeCommand(logsResponse.getData()).getData();
        byte[] read = new byte[1024];
        assertTrue(in.read(read) > -1, "No Logs Returned");
    }

    @Test(dataProvider = "app")
    public void testAppCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppCommand(app.get("name"));
        CommandResponse response = connection.executeCommand(cmd);
        assertEquals(response.get("name"), app.get("name"));
    }

    @Test(dataProvider = "app")
    public void testListAppsCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppsCommand();
        CommandResponse response = connection.executeCommand(cmd);
        assertNotNull(response.get(app.get("name")));
    }

    @Test(dataProvider = "app")
    public void testDestroyAppCommand(JsonMapResponse app) throws IOException {
        Command cmd = new AppDestroyCommand(app.get("name"));
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
        Command cmd = new ConfigAddCommand(app.get("name"), "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");
        CommandResponse response = connection.executeCommand(cmd);
    }

    @Test(dataProvider = "app")
    public void testConfigCommand(JsonMapResponse app) {
        addConfig(app, "FOO", "BAR");
        Command<JsonMapResponse> cmd = new ConfigCommand(app.get("name"));
        JsonMapResponse response = connection.executeCommand(cmd);
        assertNotNull(response.get("FOO"));
        assertEquals(response.get("FOO"), "BAR");
    }

    @Test(dataProvider = "app",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "FOO is not present.")
    public void testConfigRemoveCommand(JsonMapResponse app) {
        addConfig(app, "FOO", "BAR", "JOHN", "DOE");
        Command<JsonMapResponse> removeCommand = new ConfigRemoveCommand(app.get("name"), "FOO");
        connection.executeCommand(removeCommand);

        Command<JsonMapResponse> listCommand = new ConfigCommand(app.get("name"));
        JsonMapResponse response = connection.executeCommand(listCommand);

        assertNotNull(response.get("JOHN"), "Config var 'JOHN' should still exist, but it's not there.");
        response.get("FOO");
    }

    @Test(dataProvider = "app")
    public void testProcessCommand(JsonMapResponse app) {
        Command<JsonArrayResponse> cmd = new ProcessCommand(app.get("name"));
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response.getData(), "Expected a non-null response for a new app, but the data was null.");
        assertEquals(response.getData().size(), 1);
    }

    @Test(dataProvider = "app")
    public void testScaleCommand(JsonMapResponse app) {
        Command<EmptyResponse> cmd = new ScaleCommand(app.get("name"), "web", 1);
        EmptyResponse response = connection.executeCommand(cmd);
    }
    
    @Test(dataProvider = "app")
    public void testRestartCommand(JsonMapResponse app) {
        Command<EmptyResponse> cmd = new RestartCommand(app.get("name"));
        EmptyResponse response = connection.executeCommand(cmd);
    }
    
    @Test
    public void testListAddons() {
        Command<JsonArrayResponse> cmd = new AddonCommand();
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response, "Expected a response from listing addons, but the result is null.");
    }
    
    @Test(dataProvider = "app")
    public void testListAppAddons(JsonMapResponse app) {
        Command<JsonArrayResponse> cmd = new AppAddonCommand(app.get("name"));
        JsonArrayResponse response = connection.executeCommand(cmd);
        assertNotNull(response);
        assertTrue(response.getData().size() > 0, "Expected at least one addon to be present.");
        assertNotNull(response.get("logging:basic"));
    }
}