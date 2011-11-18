package com.heroku.api.command;

import com.google.inject.Inject;
import com.heroku.api.HerokuStack;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.command.app.AppCreate;
import com.heroku.api.command.app.AppDestroy;
import com.heroku.api.command.config.ConfigAdd;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.command.response.Unit;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.HerokuAPIException;
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
@Guice(moduleFactory = TestModuleFactory.class)
public abstract class BaseCommandIntegrationTest {

    @Inject
    Connection<?> connection;

    private List<JsonMapResponse> apps = new ArrayList<JsonMapResponse>();

    @DataProvider
    public Object[][] app() throws IOException {
        CommandConfig config = new CommandConfig().onStack(HerokuStack.Cedar);

        AppCreate cmd = new AppCreate("Cedar");
        JsonMapResponse response = connection.executeCommand(cmd);

        apps.add(response);

        return new Object[][]{{response}};
    }

    @AfterTest
    public void deleteTestApps() throws IOException {
        for (JsonMapResponse res : apps) {
            CommandConfig config = new CommandConfig()
                    .onStack(HerokuStack.Cedar)
                    .app(res.get("name"));

            try {
                Command cmd = new AppDestroy(res.get("name"));
                connection.executeCommand(cmd);
            } catch (HerokuAPIException e) {
                // quietly clean up apps created. if the destroy fails, it's ok because it might
                // have been deleted before we get to it here.
            }
        }
    }

    protected void addConfig(CommandResponse app, String... nameValuePairs) {
        if (nameValuePairs.length != 0 && (nameValuePairs.length % 2) != 0) {
            throw new RuntimeException("Config must have an equal number of name and value pairs.");
        }

        StringBuffer jsonConfig = new StringBuffer();
        jsonConfig = jsonConfig.append("{");
        String separator = "";

        for (int i = 0; i < nameValuePairs.length; i = i + 2) {
            jsonConfig = jsonConfig.append(
                    String.format("%s\"%s\":\"%s\"", separator, nameValuePairs[i], nameValuePairs[i + 1])
            );
            separator = ",";
        }

        jsonConfig = jsonConfig.append("}");

        Command<Unit> cmd = new ConfigAdd(app.get("name").toString(), new String(jsonConfig));
        connection.executeCommand(cmd);
    }
}