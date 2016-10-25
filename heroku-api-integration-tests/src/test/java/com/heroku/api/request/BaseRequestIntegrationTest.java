package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.*;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.config.ConfigUpdate;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.response.Unit;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    Connection connection;

    private List<App> apps = new ArrayList<App>();
    protected IntegrationTestConfig.TestUser sharingUser;

    static String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();


    @DataProvider(parallel = true)
    public Object[][] app() {
        return new Object[][]{{getApp()}};
    }

    @DataProvider(parallel = true)
    public Object[][] newApp() {
        return new Object[][]{{createApp()}};
    }

    public App getApp() {
        if (apps.size() > 0)
            return apps.get(0);

        return createApp();
    }

    public App createApp() {
        System.out.println("Creating app...");
        App app = connection.execute(new AppCreate(new App().on(Heroku.Stack.Cedar14)), apiKey);
        apps.add(app);
        System.out.format("%s created\n", app.getName());
        return app;
    }

    @BeforeSuite
    public void deleteExistingKeys() throws InterruptedException {
        for (IntegrationTestConfig.TestUser tu : IntegrationTestConfig.CONFIG.getTestUsers()) {
            HerokuAPI api = new HerokuAPI(tu.getApiKey());
            deleteKeys(api.listKeys());
        }
    }
    
    @BeforeSuite
    public void deleteExistingApps() throws InterruptedException {
        for (IntegrationTestConfig.TestUser tu : IntegrationTestConfig.CONFIG.getTestUsers()) {
            HerokuAPI api = new HerokuAPI(tu.getApiKey());
            deleteApps(api.listApps());
        }
    }
    
    @AfterSuite
    public void closeConnection() {
        connection.close();
    }

    @BeforeClass
    public void setupSharingUser() {
        for (IntegrationTestConfig.TestUser tu : IntegrationTestConfig.CONFIG.getTestUsers()) {
            if (!tu.isDefaultUser()) {
                sharingUser = tu;
                return;
            }
        }
        fail("Unable to get a non-default test user for sharing purposes.");
    }

    @AfterClass(alwaysRun = true)
    public void deleteTestApps() throws IOException, InterruptedException {
        deleteApps(apps);
    }

    void deleteApps(List<App> appsToDelete) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (final App res : appsToDelete) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.format("Deleting app %s\n", res.getName());
                    deleteApp(res.getName());
                    System.out.format("Deleted app %s\n", res.getName());
                }
            });
        }
        // await termination of all the threads to complete app deletion.
        executorService.shutdown();
        executorService.awaitTermination(300L, TimeUnit.SECONDS);
        System.out.format("Deleted apps in %dms\n", (System.currentTimeMillis() - start));
    }

    void deleteKeys(List<Key> keysToDelete) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (final Key res : keysToDelete) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.format("Deleting key %s\n", res.getComment());
                    deleteKey(res.getId());
                    System.out.format("Deleted key %s\n", res.getComment());
                }
            });
        }
        // await termination of all the threads to complete app deletion.
        executorService.shutdown();
        executorService.awaitTermination(300L, TimeUnit.SECONDS);
        System.out.format("Deleted keys in %dms\n", (System.currentTimeMillis() - start));
    }

    public void deleteKey(String sshkey) {
        try {
            connection.execute(new KeyRemove(sshkey), apiKey);
        } catch (RequestFailedException e) {
            if (e.getStatusCode() != Http.Status.FORBIDDEN.statusCode) {
                throw e;
            }
        }
    }

    public void deleteApp(String appName) {
        try {
            connection.execute(new AppDestroy(appName), apiKey);
        } catch (RequestFailedException e) {
            if (e.getStatusCode() != Http.Status.FORBIDDEN.statusCode) {
                throw e;
            }
        }
    }

    protected void addConfig(App app, String... nameValuePairs) {
        if (nameValuePairs.length != 0 && (nameValuePairs.length % 2) != 0) {
            throw new RuntimeException("Config must have an equal number of name and value pairs.");
        }

        Map<String,String> configVars = new HashMap<String,String>();
        for (int i = 0; i < nameValuePairs.length; i = i + 2) {
            configVars.put(nameValuePairs[i], nameValuePairs[i + 1]);
        }

        Request<Unit> req = new ConfigUpdate(app.getName(), configVars);
        connection.execute(req, apiKey);
    }

    void assertLogIsReadable(LogStreamResponse logsResponse) throws IOException {
        InputStream in = logsResponse.openStream();
        try {
            in = logsResponse.openStream();
            in.close();
        } finally {
            in.close();
        }
    }

    void assertCollaboratorNotPresent(String collabEmail, CollabList collabList) {
        List<Collaborator> collaborators = connection.execute(collabList, apiKey);
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getEmail().equals(collabEmail)) {
                fail("Collaborator was not removed");
            }
        }
    }

}