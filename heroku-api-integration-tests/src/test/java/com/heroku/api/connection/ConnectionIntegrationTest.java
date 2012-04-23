package com.heroku.api.connection;

import com.google.inject.Inject;
import com.heroku.api.IntegrationTestConfig;
import com.heroku.api.LoginVerification;
import com.heroku.api.TestModuleFactory;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.request.login.BasicAuthLogin;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.heroku.api.IntegrationTestConfig.CONFIG;
import static org.testng.Assert.assertEquals;

/**
 * Integration tests for authenticating with the Heroku API.
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class ConnectionIntegrationTest {

    @Inject
    Connection connection;

    @Test(groups = "integration")
    public void testValidUsernameAndPassword() throws IOException {
        IntegrationTestConfig.TestUser testUser = CONFIG.getDefaultUser();
        LoginVerification verification = connection.execute(new BasicAuthLogin(testUser.getUsername(), testUser.getPassword()), testUser.getApiKey());
        assertEquals(verification.getEmail(), testUser.getUsername());
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
        connection.execute(new BasicAuthLogin(username, password), CONFIG.getDefaultUser().getApiKey());
    }
}
