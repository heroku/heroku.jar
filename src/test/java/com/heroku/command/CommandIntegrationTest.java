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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;


/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(modules = ConnectionTestModule.class)
public class CommandIntegrationTest {

    private static final String KEY_COMMENT = "foo@bar";

    public static final String DEMO_EMAIL = "jw+demo@heroku.com";

    @Inject
    HerokuConnection conn;

    String appName;

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
    public void testKeysAddCommand() throws IOException, HerokuAPIException {
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

    @Test
    public void testSharingAddCommand() throws IOException, HerokuAPIException {
        testCreateAppCommand();

        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.app, appName);
        config.set(HerokuRequestKeys.collaborator, DEMO_EMAIL);

        HerokuCommand cmd = new HerokuSharingAddCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);
    }

    @Test(dependsOnMethods={"testSharingAddCommand"})
    public void testSharingRemoveCommand() throws IOException, HerokuAPIException {
        testSharingAddCommand();

        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.app, appName);
        config.set(HerokuRequestKeys.collaborator, DEMO_EMAIL);

        HerokuCommand cmd = new HerokuSharingRemoveCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);
        assertNotNull(response);

        testDestroyAppCommand();
    }


    @Test
    public void testSharingTransferCommand() throws IOException, HerokuAPIException {
        testSharingAddCommand();

        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.app, appName);
        config.set(HerokuRequestKeys.collaborator, DEMO_EMAIL);

        HerokuCommand cmd = new HerokuSharingTransferCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);

        // this app can't be destroyed after it has been transferred - until we add a second heroku user to this setup
    }

    @Test
    public void testConfigAddCommand() throws IOException, HerokuAPIException {
        testCreateAppCommand();

        HerokuCommandConfig<HerokuRequestKeys> config = new HerokuCommandConfig<HerokuRequestKeys>();
        config.set(HerokuRequestKeys.app, appName);
        config.set(HerokuRequestKeys.configvars, "{\"FOO\":\"bar\", \"BAR\":\"foo\"}");

        HerokuCommand cmd = new HerokuConfigAddCommand(config);
        HerokuCommandResponse response = cmd.execute(conn);

        testDestroyAppCommand();
    }

}