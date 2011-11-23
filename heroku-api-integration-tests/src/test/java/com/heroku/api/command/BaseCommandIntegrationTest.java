package com.heroku.api.command;

import com.google.inject.Inject;
import com.heroku.api.Heroku;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.command.app.AppCreate;
import com.heroku.api.command.app.AppDestroy;
import com.heroku.api.command.config.ConfigAdd;
import com.heroku.api.command.log.LogStreamResponse;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.command.response.Unit;
import com.heroku.api.connection.Connection;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

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
        CommandConfig config = new CommandConfig().onStack(Heroku.Stack.Cedar);

        AppCreate cmd = new AppCreate("Cedar");
        JsonMapResponse response = connection.executeCommand(cmd);

        apps.add(response);

        return new Object[][]{{response}};
    }

    @AfterSuite
    public void closeConnection() {
        connection.close();
    }
    
    @AfterTest(alwaysRun = true)
    public void deleteTestApps() throws IOException {
        for (JsonMapResponse res : apps) {
            Command<Unit> cmd = new AppDestroy(res.get("name"));
            connection.executeCommand(cmd);
        }
    }
    
    public void deleteApp(String appName) {
        connection.executeCommand(new AppDestroy(appName));
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

    void assertLogIsReadable(LogStreamResponse logsResponse) throws IOException {
        InputStream in = logsResponse.openStream();
        try {
            in = logsResponse.openStream();
            assertTrue(in.read(new byte[1024]) > -1, "No logs returned");
        } catch (Exception e) {
            fail("Unable to read logs", e);
        } finally {
            in.close();
        }
    }
}