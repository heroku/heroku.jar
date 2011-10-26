package com.heroku.command;

import com.google.inject.Inject;
import com.heroku.ConnectionTestModule;
import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
//import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;

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


}
