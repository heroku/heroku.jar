package com.heroku.api.request;

import com.google.inject.Inject;
import com.heroku.api.*;
import com.heroku.api.connection.Connection;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppDestroy;
import com.heroku.api.request.builds.BuildCreate;
import com.heroku.api.request.builds.BuildInfo;
import com.heroku.api.request.config.ConfigUpdate;
import com.heroku.api.request.dynos.DynoList;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.log.LogStreamResponse;
import com.heroku.api.request.sharing.CollabList;
import com.heroku.api.request.sources.SourceCreate;
import com.heroku.api.request.team.TeamAppCreate;
import com.heroku.api.request.team.TeamCreate;
import com.heroku.api.request.team.TeamDestroy;
import com.heroku.api.response.Unit;
import com.heroku.api.util.Range;
import org.testng.annotations.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
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
    private List<App> javaApps = new ArrayList<App>();
    private List<Team> teams = new ArrayList<Team>();
    protected IntegrationTestConfig.TestUser sharingUser;

    static String apiKey = IntegrationTestConfig.CONFIG.getDefaultUser().getApiKey();

    @DataProvider(parallel = true)
    public Object[][] app() {
        return new Object[][]{{getApp()}};
    }

    @DataProvider(parallel = true)
    public Object[][] newApp() {
        return new Object[][]{{createApp("new")}};
    }

    @DataProvider(parallel = true)
    public Object[][] javaApp() {
        return new Object[][]{{getJavaApp()}};
    }

    public App getApp() {
        if (apps.size() > 0)
            return apps.get(0);

        return createApp();
    }

    @DataProvider
    public Object[][] teamApp() {
        HerokuAPI api = new HerokuAPI(apiKey);
        Team team;
        if (teams.size() > 0) {
            team = teams.get(0);
        } else {
            System.out.println("Creating team...");
            team = api.createTeam("herokujar-" + new Random().nextInt(999999));
            System.out.format("team %s created\n", team.getName());
            teams.add(team);
        }

        System.out.println("Creating team app...");
        TeamApp app = api.createTeamApp(new TeamApp().withTeam(team).on(Heroku.Stack.Heroku16));

        System.out.format("app %s created\n", app.getName());
        return new Object[][]{{app}};
    }

    public App createApp() {
        return createApp("");
    }

    public App createApp(String type) {
        System.out.println("Creating " + (type.isEmpty() ? type : type + " ") + "app...");
        App app = connection.execute(new AppCreate(new App().on(Heroku.Stack.Heroku18)), apiKey);
        apps.add(app);
        System.out.format("%s created\n", app.getName());
        return app;
    }

    public App getJavaApp() {
        App app;
        if (javaApps.size() > 0) {
            app = javaApps.get(0);
        } else {
            app = createApp("Java");
            javaApps.add(app);
        }
        Source source = connection.execute(new SourceCreate(), apiKey);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("java-repo.tgz").getFile());

        try {
            uploadFile(source.getSource_blob().getPut_url(), file);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Build build = connection.execute(new BuildCreate(app.getName(),
            new Build(
                source.getSource_blob().getGet_url(),
                "v1",
                new String[]{"https://codon-buildpacks.s3.amazonaws.com/buildpacks/heroku/java.tgz"}
            )
        ), apiKey);

        while (!"succeeded".equals(build.getStatus())) {
            if ("failed".equals(build.getStatus())) {
                System.out.println(build.getOutput_stream_url());
                fail("Java build failed");
                break;
            } else if (!"pending".equals(build.getStatus())) {
                fail("Java build has invalid status: " + build.getStatus());
                break;
            } else {
                build = connection.execute(new BuildInfo(app.getName(), build.getId()), apiKey);
            }
        }

        Range<Dyno> dynos = connection.execute(new DynoList(app.getName()), apiKey);
        Integer attempts = 0;
        while (dynos.isEmpty()) {
            if (attempts > 10) {
                fail("Failed to start dynos for " + app.getName());
                break;
            }
            dynos = connection.execute(new DynoList(app.getName()), apiKey);
            attempts++;
            try { Thread.sleep(2000); } catch (InterruptedException e) { }
        }

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
        deleteTeams(teams);
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

    void deleteTeams(List<Team> teamsToDelete) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (final Team res : teamsToDelete) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.format("Deleting team %s\n", res.getName());
                    deleteTeam(res.getName());
                    System.out.format("Deleted team %s\n", res.getName());
                }
            });
        }
        // await termination of all the threads to complete app deletion.
        executorService.shutdown();
        executorService.awaitTermination(300L, TimeUnit.SECONDS);
        System.out.format("Deleted teams in %dms\n", (System.currentTimeMillis() - start));
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

    void deleteKey(String sshkey) {
        try {
            connection.execute(new KeyRemove(sshkey), apiKey);
        } catch (RequestFailedException e) {
            if (e.getStatusCode() != Http.Status.FORBIDDEN.statusCode) {
                throw e;
            }
        }
    }

    void deleteApp(String appName) {
        try {
            connection.execute(new AppDestroy(appName), apiKey);
        } catch (RequestFailedException e) {
            if (e.getStatusCode() != Http.Status.FORBIDDEN.statusCode) {
                throw e;
            }
        }
    }

    void deleteTeam(String teamName) {
        try {
            HerokuAPI api = new HerokuAPI(apiKey);
            for (TeamApp app : api.listTeamApps(teamName)) {
                deleteApp(app.getName());
            }

            connection.execute(new TeamDestroy(teamName), apiKey);
        } catch (RequestFailedException e) {
            if (e.getStatusCode() != Http.Status.FORBIDDEN.statusCode) {
                throw e;
            }
        }
    }

    void addConfig(App app, String... nameValuePairs) {
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

    void assertCollaboratorNotPresent(String collabEmail, CollabList collabList) {
        List<Collaborator> collaborators = connection.execute(collabList, apiKey);
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getEmail().equals(collabEmail)) {
                fail("Collaborator was not removed");
            }
        }
    }

    void uploadFile(String urlString, File file) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");

        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = httpCon.getOutputStream();

        Integer c;
        while ((c = inputStream.read()) != -1) {
            outputStream.write(c);
        }
        outputStream.close();

        httpCon.getInputStream();
    }

}