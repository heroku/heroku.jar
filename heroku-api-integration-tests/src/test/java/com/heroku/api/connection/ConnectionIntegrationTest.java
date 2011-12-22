package com.heroku.api.connection;

import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.request.login.BasicAuthLogin;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.CONFIG;

/**
 * Integration tests for authenticating with the Heroku API.
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class ConnectionIntegrationTest {

    @Test(groups = "integration")
    public void testValidUsernameAndPassword() throws IOException {
        IntegrationTestConfig.TestUser testUser = CONFIG.getDefaultUser();
        Connection conn = new HttpClientConnection(new BasicAuthLogin(testUser.getUsername(), testUser.getPassword()));
    }

    @DataProvider
    public Object[][] invalidUsernamesAndPasswords() {
        return new Object[][]{
                {null, null},
                {"", ""},
                {"rodneyMullen@powell.peralta.bones.brigade", "fakeUsernameAndPassword"}
        };
    }

    @Test(groups = "integration",
            dataProvider = "invalidUsernamesAndPasswords",
            expectedExceptions = RequestFailedException.class)
    public void testInvalidUsernameAndPassword(String username, String password) throws IOException {
        new HttpClientConnection(new BasicAuthLogin(username, password));
    }
}
