package com.heroku.command;

import com.google.inject.Inject;
import com.heroku.ConnectionTestModule;
import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import com.heroku.util.OpenSSHKeyUtil;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

//import org.testng.Assert;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(modules = ConnectionTestModule.class)
public class CommandIntegrationTest {

    @Inject
    HerokuConnection conn;

    String appName;

    private static final String KEY_COMMENT = "foo@bar";

    public CommandIntegrationTest() {
        // setup verbose logging
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
    }

    @Test
    public void testCreateAppCommand() throws HerokuAPIException, IOException {
        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.stack, "cedar");
        config.set(HerokuRequestKeys.remote, "heroku");
        config.set(HerokuRequestKeys.timeout, "10");
        config.set(HerokuRequestKeys.addons, "");
        HerokuCommand cmd = new HerokuAppCreateCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
        appName = response.get("name").toString();
    }

    @Test(dependsOnMethods = "testCreateAppCommand")
    public void testDestroyAppCommand() throws IOException, HerokuAPIException {
        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.name, appName);
        HerokuCommand cmd = new HerokuAppDestroyCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);
    }

    @Test
    public void testKeysAddCommand() throws IOException, HerokuAPIException, NoSuchAlgorithmException {
        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();

        String sshkey = OpenSSHKeyUtil.encodeOpenSSHPublicKeyString(OpenSSHKeyUtil.generateRSAPublicKey(), KEY_COMMENT);
        
        config.set(HerokuRequestKeys.sshkey, sshkey);
        HerokuCommand cmd = new HerokuKeysAddCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);
    }

    @Test(dependsOnMethods={"testKeysAddCommand"})
    public void testKeysRemoveCommand() throws IOException, HerokuAPIException {
        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();

        config.set(HerokuRequestKeys.name, KEY_COMMENT);
        HerokuCommand cmd = new HerokuKeysRemoveCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);
    }

    @Test(expectedExceptions = HerokuAPIException.class)
    public void testKeysAddCommandWithDuplicateKey() throws IOException, HerokuAPIException {
        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        String sshkey = FileUtils.readFileToString(new File(getClass().getResource("/id_rsa.pub").getFile()));
        config.set(HerokuRequestKeys.sshkey, sshkey);
        HerokuCommand cmd = new HerokuKeysAddCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);
    }

}