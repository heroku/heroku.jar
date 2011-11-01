package com.heroku.command;

import com.heroku.HerokuStack;
import com.heroku.connection.HerokuAPIException;
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


}
