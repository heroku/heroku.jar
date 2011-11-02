package com.heroku.command;

import com.heroku.HerokuStack;
import com.heroku.connection.HerokuAPIException;
import com.heroku.util.OpenSSHKeyUtil;
import org.apache.commons.io.FileUtils;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CommandIntegrationTest extends BaseCommandIntegrationTest {

    // test app gets transferred to this user until we have a second user in auth-test.properties
    private static final String DEMO_EMAIL = "jw+demo@heroku.com";

    @Test
    public void testCreateAppCommand() throws HerokuAPIException, IOException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        HerokuCommand cmd = new HerokuAppCreateCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
    }

    @Test(dataProvider = "app")
    public void testListAppsCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        HerokuCommand cmd = new HerokuAppsCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertNotNull(response.get(app.get("name").toString()));
    }

    @Test(dataProvider = "app")
    public void testDestroyAppCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());

        HerokuCommand cmd = new HerokuAppDestroyCommand(config);

        HerokuCommandResponse response = cmd.execute(connection);

        assertEquals(response.isSuccess(), true);
    }

    @Test(dataProvider = "app")
    public void testSharingAddCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());
        config.set(HerokuRequestKey.collaborator, DEMO_EMAIL);

        HerokuCommand cmd = new HerokuSharingAddCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

    // if we do this then we will no longer be able to remove the app
    // we need two users in auth-test.properties so that we can transfer it to one and still control it,
    // rather than transferring it to a black hole
    @Test(dataProvider = "app", dependsOnMethods = {"testSharingAddCommand"})
    public void testSharingTransferCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());
        config.set(HerokuRequestKey.collaborator, DEMO_EMAIL);

        HerokuCommand cmd = new HerokuSharingTransferCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

    @Test(dataProvider = "app", dependsOnMethods = {"testSharingAddCommand"})
    public void testSharingRemoveCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());
        config.set(HerokuRequestKey.collaborator, connection.getEmail());

        HerokuCommand cmd = new HerokuSharingRemoveCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

    @Test(dataProvider = "app")
    public void testConfigAddCommand(HerokuCommandResponse app) throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());
        config.set(HerokuRequestKey.configvars, "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");

        HerokuCommand cmd = new HerokuConfigAddCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

}