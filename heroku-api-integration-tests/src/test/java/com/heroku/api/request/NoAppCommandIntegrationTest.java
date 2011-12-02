package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.model.Key;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertNotNull;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class NoAppCommandIntegrationTest {

    private static final String PUBLIC_KEY_COMMENT = "foo@bar";

    @Inject
    Connection<?> connection;

    // doesn't need an app
    @Test
    public void testKeysAddCommand() throws JSchException, IOException {
        JSch jsch = new JSch();
        KeyPair keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA);

        ByteArrayOutputStream publicKeyOutputStream = new ByteArrayOutputStream();
        keyPair.writePublicKey(publicKeyOutputStream, PUBLIC_KEY_COMMENT);
        publicKeyOutputStream.close();
        String sshPublicKey = new String(publicKeyOutputStream.toByteArray());

        KeyAdd cmd = new KeyAdd(sshPublicKey);
        connection.execute(cmd);

    }

    // doesn't need an app
    @Test(dependsOnMethods = {"testKeysAddCommand"})
    public void testKeysRemoveCommand() {
        KeyRemove cmd = new KeyRemove(PUBLIC_KEY_COMMENT);
        connection.execute(cmd);
    }

    // doesn't need an app
    // currently uses a key associated with another user but really should do the following:
    // add a key to one user, then try to add the same key to another user
    // but this depends on having two users in auth-test.properties
    @Test(expectedExceptions = HerokuAPIException.class)
    public void testKeysAddCommandWithDuplicateKey() throws IOException {
        String sshkey = FileUtils.readFileToString(new File(getClass().getResource("/id_rsa.pub").getFile()));
        KeyAdd cmd = new KeyAdd(sshkey);
        connection.execute(cmd);
    }

    @Test
    public void testKeyListCommand() {
        KeyList keyListRequest = new KeyList();
        List<Key> keyListResponse = connection.execute(keyListRequest);
        assertNotNull(keyListResponse);
    }

}