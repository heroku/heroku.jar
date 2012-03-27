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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class KeysRequestIntegrationTest {

    private final String API_KEY = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @Inject
    Connection connection;

    @BeforeSuite
    public void deleteAllKeys() {
        connection.execute(new KeysRemoveAll(), API_KEY);
    }
    
    @DataProvider
    public Object[][] publicKey() throws JSchException, IOException {
        String comment = getComment();
        String pubKey = getPublicKey(comment);
        return new Object[][]{{pubKey, comment}};
    }

    @Test(dataProvider = "publicKey")
    public void testKeysAddCommand(String pubKey, String comment) throws JSchException, IOException {
        KeyAdd cmd = new KeyAdd(pubKey);
        Unit response = connection.execute(cmd, API_KEY);
        assertTrue(keyIsPresent(comment), comment + " should have been added.");
    }

    @Test(dataProvider = "publicKey")
    public void testKeysRemoveCommand(String pubKey, String comment) {
        connection.execute(new KeyAdd(pubKey), API_KEY);
        KeyRemove cmd = new KeyRemove(comment);
        Unit response = connection.execute(cmd, API_KEY);
        assertFalse(keyIsPresent(comment));
    }

    @Test(dataProvider = "publicKey", expectedExceptions = HerokuAPIException.class)
    public void testKeysAddCommandWithDuplicateKey(String pubKey, String comment) throws IOException {
        KeyAdd cmd = new KeyAdd(pubKey);
        connection.execute(cmd, API_KEY);

        KeyAdd duplicateKeyCommand = new KeyAdd(pubKey);
        connection.execute(duplicateKeyCommand, IntegrationTestConfig.CONFIG.getOtherUser().getApiKey());
    }

    @Test(dataProvider = "publicKey")
    public void testKeyListCommand(String pubKey, String comment) {
        connection.execute(new KeyAdd(pubKey), API_KEY);
        KeyList keyListRequest = new KeyList();
        List<Key> keyListResponse = connection.execute(keyListRequest, API_KEY);
        Assert.assertTrue(keyIsPresent(comment, keyListResponse), comment + " should have been present.");
    }

    public String getPublicKey(String comment) throws JSchException, IOException {
        JSch jsch = new JSch();
        KeyPair keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA);

        ByteArrayOutputStream publicKeyOutputStream = new ByteArrayOutputStream();
        keyPair.writePublicKey(publicKeyOutputStream, comment);
        publicKeyOutputStream.close();
        return new String(publicKeyOutputStream.toByteArray());
    }

    public boolean keyIsPresent(String comment) {
        return keyIsPresent(comment, connection.execute(new KeyList(), API_KEY));
    }

    public boolean keyIsPresent(String comment, List<Key> keyList) {
        for (Key k : keyList) {
            if (k.getContents().contains(comment)) {
                return true;
            }
        }
        return false;
    }

    public String getComment() {
        return "foo@bar" + Math.round(Math.random() * 100);
    }
}