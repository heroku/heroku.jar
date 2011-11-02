package com.heroku.api.command;

import com.google.inject.Inject;
import com.heroku.api.ConnectionTestModule;
import com.heroku.api.HerokuStack;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import com.heroku.util.OpenSSHKeyUtil;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(modules = ConnectionTestModule.class)
public class NoAppCommandIntegrationTest {

    private static final String PUBLIC_KEY_COMMENT = "foo@bar";

    @Inject
    HerokuConnection connection;

    // doesn't need an app
    @Test
    public void testKeysAddCommand() throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        String sshkey = OpenSSHKeyUtil.encodeOpenSSHPublicKeyString(OpenSSHKeyUtil.generateRSAPublicKey(), PUBLIC_KEY_COMMENT);

        config.set(HerokuRequestKey.sshkey, sshkey);
        HerokuCommand cmd = new HerokuKeysAddCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

    // doesn't need an app
    @Test(dependsOnMethods = {"testKeysAddCommand"})
    public void testKeysRemoveCommand() throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        config.set(HerokuRequestKey.name, PUBLIC_KEY_COMMENT);
        HerokuCommand cmd = new HerokuKeysRemoveCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertTrue(response.isSuccess());
    }

    // doesn't need an app
    // currently uses a key associated with another user but really should do the following:
    // add a key to one user, then try to add the same key to another user
    // but this depends on having two users in auth-test.properties
    @Test
    public void testKeysAddCommandWithDuplicateKey() throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);
        String sshkey = FileUtils.readFileToString(new File(getClass().getResource("/id_rsa.pub").getFile()));
        config.set(HerokuRequestKey.sshkey, sshkey);
        HerokuCommand cmd = new HerokuKeysAddCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);
        assertFalse(response.isSuccess());
    }

}