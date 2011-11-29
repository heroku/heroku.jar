package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.Heroku;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.request.app.App;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.response.JsonMapResponse;
import com.heroku.api.request.response.Unit;
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
public abstract class BaseRequestIntegrationTest {

    @Inject
    Connection<?> connection;

    private List<App> apps = new ArrayList<App>();

    @DataProvider
    public Object[][] app() throws IOException {
        RequestConfig config = new RequestConfig().onStack(Heroku.Stack.Cedar);

        AppCreate cmd = new AppCreate("Cedar");
        App app = connection.execute(cmd);

        apps.add(app);

        return new Object[][]{{app}};
    }

    @AfterSuite
    public void closeConnection() {
        connection.close();
    }
    
    @AfterTest(alwaysRun = true)
    public void deleteTestApps() throws IOException {
        for (App res : apps) {
            Request<Unit> req = new AppDestroy(res.name());
            connection.execute(req);
        }
    }
    
    public void deleteApp(String appName) {
        connection.execute(new AppDestroy(appName));
    }

    protected void addConfig(App app, String... nameValuePairs) {
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

        Request<Unit> req = new ConfigAdd(app.name(), new String(jsonConfig));
        connection.execute(req);
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