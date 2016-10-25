package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.Key;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyList;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.response.Unit;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertTrue;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class KeysRequestIntegrationTest {

    private final String API_KEY = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    private final List<String> pubKeyComments = new ArrayList<String>();

    @Inject
    Connection connection;

    @AfterClass(alwaysRun = true)
    public void deleteAllKeys() {
        // delete keys individually to avoid race conditions.
        // if a key is not found, ignore the failure because it's already been deleted.
        for (String k : pubKeyComments) {
            try {
                connection.execute(new KeyRemove(k), API_KEY);
            } catch (RequestFailedException e) {
                if (!Http.Status.NOT_FOUND.equals(e.getStatusCode())) {
                    throw e;
                }
            }
        }
    }

    @DataProvider
    public Object[][] publicKey() throws JSchException, IOException {
        String comment = getComment();
        pubKeyComments.add(comment);
        String pubKey = getPublicKey(comment);
        return new Object[][]{{pubKey, comment}};
    }

    @Test(dataProvider = "publicKey")
    public void testKeysAddCommand(String pubKey, String comment) throws JSchException, IOException {
        KeyAdd cmd = new KeyAdd(pubKey);
        Unit response = connection.execute(cmd, API_KEY);
        assertTrue(keyIsPresent(comment), comment + " should have been added.");
    }

    // TODO: Re-enable
    //@Test(dataProvider = "publicKey")
    //public void testKeysRemoveCommand(String pubKey, String comment) {
    //    connection.execute(new KeyAdd(pubKey), API_KEY);
    //    KeyRemove cmd = new KeyRemove(comment);
    //    Unit response = connection.execute(cmd, API_KEY);
    //    assertFalse(keyIsPresent(comment));
    //}

    @Test(dataProvider = "publicKey", expectedExceptions = RequestFailedException.class, singleThreaded = true)
    public void testKeysAddCommandWithDuplicateKey(String pubKey, String comment) throws IOException {
        KeyAdd keyAdd = new KeyAdd(pubKey);
        connection.execute(keyAdd, API_KEY);
        connection.execute(keyAdd, IntegrationTestConfig.CONFIG.getOtherUser().getApiKey());
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
            if (comment.equals(k.getComment())) {
                return true;
            }
        }
        return false;
    }

    public String getComment() {
        return "j@" + UUID.randomUUID().toString() + ".com";
    }
}
