package com.heroku.api.command;

import com.heroku.api.HerokuStack;
//import org.testng.Assert;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class CommandIntegrationTest extends BaseCommandIntegrationTest {

    @Test
    public void testCreateAppCommand() throws IOException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        HerokuCommand cmd = new HerokuAppCreateCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertNotNull(response.get("id"));
        assertEquals(response.get("stack").toString(), "cedar");
    }

    @Test(dataProvider = "app")
    public void testAppCommand(HerokuCommandResponse app) throws IOException {
        HerokuCommandConfig config = new HerokuCommandConfig()
                .onStack(HerokuStack.Cedar)
                .app(app.get("name").toString());

        HerokuCommand cmd = new HerokuAppCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertEquals(response.get("name"), app.get("name"));
    }

    @Test(dataProvider = "app")
    public void testListAppsCommand(HerokuCommandResponse app) throws IOException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        HerokuCommand cmd = new HerokuAppsCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        assertNotNull(response.get(app.get("name").toString()));
    }

    @Test(dataProvider = "app")
    public void testDestroyAppCommand(HerokuCommandResponse app) throws IOException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar).app(app.get("name").toString());

        HerokuCommand cmd = new HerokuAppDestroyCommand(config);

        HerokuCommandResponse response = cmd.execute(connection);

        assertEquals(response.isSuccess(), true);
    }


}
