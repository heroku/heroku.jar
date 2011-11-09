package com.heroku.api.command;

import com.google.inject.Inject;
import com.heroku.api.ConnectionTestModule;
import com.heroku.api.HerokuStack;
import com.heroku.api.connection.Connection;
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
    Connection connection;

    private List<CommandResponse> apps = new ArrayList<CommandResponse>();

    @DataProvider
    public Object[][] app() throws IOException {
        CommandConfig config = new CommandConfig().onStack(HerokuStack.Cedar);

        Command cmd = new AppCreateCommand(config);
        CommandResponse response = connection.executeCommand(cmd);

        apps.add(response);

        return new Object[][]{{response}};
    }

    @AfterTest
    public void deleteTestApps() throws IOException {
        for (CommandResponse res : apps) {
            CommandConfig config = new CommandConfig()
                    .onStack(HerokuStack.Cedar)
                    .app(res.get("name").toString());

            // quietly clean up apps created. if the destroy fails, it's ok because it might
            // have been deleted before we get to it here.
            Command cmd = new AppDestroyCommand(config);
            connection.executeCommand(cmd);
        }
    }

}