package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.Key;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.key.KeysRemoveAll;
import com.heroku.api.response.Unit;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
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
    Connection connection;

    static String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();


    @BeforeSuite
    public void deleteAllKeys() {
        connection.execute(new KeysRemoveAll(), apiKey);
    }

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
        Unit response = connection.execute(cmd, apiKey);
        List<Key> keys = connection.execute(new KeyList(), apiKey);
        Assert.assertEquals(keys.size(), 1, "Precondition: keys should have been cleaned up.");
        Assert.assertTrue(keys.get(0).getContents().contains(PUBLIC_KEY_COMMENT));
    }

    // doesn't need an app
    @Test(dependsOnMethods = "testKeyListCommand")
    public void testKeysRemoveCommand() {
        Assert.assertTrue(keyIsPresent(PUBLIC_KEY_COMMENT), PUBLIC_KEY_COMMENT + " should have been present.");
        KeyRemove cmd = new KeyRemove(PUBLIC_KEY_COMMENT);
        Unit response = connection.execute(cmd, apiKey);
        assertNotNull(response);
    }

    @Test(expectedExceptions = HerokuAPIException.class)
    public void testKeysAddCommandWithDuplicateKey() throws IOException {
        String sshkey = FileUtils.readFileToString(new File(getClass().getResource("/id_rsa.pub").getFile()));
        KeyAdd cmd = new KeyAdd(sshkey);
        connection.execute(cmd, apiKey);

        String duplicateKey = FileUtils.readFileToString(new File(getClass().getResource("/id_rsa.pub").getFile()));
        KeyAdd duplicateKeyCommand = new KeyAdd(duplicateKey);
        connection.execute(duplicateKeyCommand, IntegrationTestConfig.CONFIG.getOtherUser().getApiKey());
    }

    @Test(dependsOnMethods = "testKeysAddCommand")
    public void testKeyListCommand() {
        KeyList keyListRequest = new KeyList();
        List<Key> keyListResponse = connection.execute(keyListRequest, apiKey);
        Assert.assertTrue(keyIsPresent(PUBLIC_KEY_COMMENT, keyListResponse), PUBLIC_KEY_COMMENT + " should have been present.");
    }

    public boolean keyIsPresent(String comment) {
        return keyIsPresent(comment, connection.execute(new KeyList(), apiKey));
    }

    public boolean keyIsPresent(String comment, List<Key> keyList) {
        for (Key k : keyList) {
            if (k.getContents().contains(comment)) {
                return true;
            }
        }
        return false;
    }
}