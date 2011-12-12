package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.Heroku;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.connection.Connection;
import com.heroku.api.model.App;
import com.heroku.api.model.Collaborator;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.config.ConfigAdd;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.response.Unit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private static List<com.heroku.api.model.App> apps = new ArrayList<com.heroku.api.model.App>();

    @DataProvider
    public Object[][] app() {
        return new Object[][]{{getApp()}};
    }
    
    @DataProvider
    public Object[][] newApp() {
        return new Object[][] {{createApp()}};
    }
    
    public App getApp() {
        if (apps.size() > 0)
            return apps.get(0);

        return createApp();
    }
    
    public App createApp() {
        System.out.println("Creating app...");
        App app = connection.execute(new AppCreate(Heroku.Stack.Cedar));
        apps.add(app);
        System.out.format("%s created\n", app.getName());
        return app;
    }

    @AfterSuite
    public void closeConnection() {
        connection.close();
    }

    @AfterClass(alwaysRun = true)
    public void deleteTestApps() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (final App res : apps) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.format("Deleting %s\n", res.getName());
                    deleteApp(res.getName());
                    System.out.format("Deleted %s\n", res.getName());
                }
            });
        }
        // await termination of all the threads to complete app deletion.
        executorService.awaitTermination(120L, TimeUnit.SECONDS);
        System.out.format("Deleted apps in %dms", (System.currentTimeMillis() - start));
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

        Request<Unit> req = new ConfigAdd(app.getName(), new String(jsonConfig));
        connection.execute(req);
    }

    void assertLogIsReadable(LogStreamResponse logsResponse) throws IOException {
        InputStream in = logsResponse.openStream();
        try {
            in = logsResponse.openStream();
            in.close();
        } catch (Exception e) {
            fail("Unable to read logs", e);
        } finally {
            in.close();
        }
    }

    void assertCollaboratorNotPresent(String collabEmail, CollabList collabList) {
        List<Collaborator> collaborators = connection.execute(collabList);
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getEmail().equals(collabEmail)) {
                fail("Collaborator was not removed");
            }
        }
    }
}