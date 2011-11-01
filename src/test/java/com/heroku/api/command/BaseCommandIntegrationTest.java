package com.heroku.api.command;

import com.google.inject.Inject;
import com.heroku.api.ConnectionTestModule;
import com.heroku.api.HerokuStack;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(modules = ConnectionTestModule.class)
public abstract class BaseCommandIntegrationTest {

    @Inject
    HerokuConnection connection;

    private List<HerokuCommandResponse> apps = new ArrayList<HerokuCommandResponse>();

    @DataProvider
    public Object[][] app() throws IOException, HerokuAPIException {
        HerokuCommandConfig config = new HerokuCommandConfig().onStack(HerokuStack.Cedar);

        HerokuCommand cmd = new HerokuAppCreateCommand(config);
        HerokuCommandResponse response = cmd.execute(connection);

        apps.add(response);

        return new Object[][]{{response}};
    }

    @AfterTest
    public void deleteTestApps() throws IOException, HerokuAPIException {
        for (HerokuCommandResponse res : apps) {
            HerokuCommandConfig config = new HerokuCommandConfig()
                    .onStack(HerokuStack.Cedar)
                    .app(res.get("name").toString());

            // quietly clean up apps created. if the destroy fails, it's ok because it might
            // have been deleted before we get to it here.
            HerokuCommand cmd = new HerokuAppDestroyCommand(config);
            cmd.execute(connection);
        }
    }

}