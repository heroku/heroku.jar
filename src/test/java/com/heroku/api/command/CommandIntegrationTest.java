package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuStack;
import org.testng.annotations.Test;

import java.io.IOException;

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
        
        assertTrue(response.isSuccess());
        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
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
        assertEquals(response.isSuccess(), true);
    }

    @Test(dataProvider = "app")
    public void testSharingAddCommand(JsonMapResponse app) throws IOException {
        Command cmd = new SharingAddCommand(app.get("name"), DEMO_EMAIL);
        CommandResponse response = connection.executeCommand(cmd);
        assertTrue(response.isSuccess());
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

        assertTrue(sharingTransferCommandResponse.isSuccess());
    }

    @Test(dataProvider = "app")
    public void testSharingRemoveCommand(JsonMapResponse app) throws IOException {
        Command sharingAddCommand = new SharingAddCommand(app.get("name"), DEMO_EMAIL);
        connection.executeCommand(sharingAddCommand);

        Command cmd = new SharingRemoveCommand(app.get("name"), DEMO_EMAIL);
        CommandResponse response = connection.executeCommand(cmd);

        assertTrue(response.isSuccess());
    }

    @Test(dataProvider = "app")
    public void testConfigAddCommand(JsonMapResponse app) throws IOException {
        Command cmd = new ConfigAddCommand(app.get("name"), "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");
        CommandResponse response = connection.executeCommand(cmd);

        assertTrue(response.isSuccess());
    }

}