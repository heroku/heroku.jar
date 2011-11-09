package com.heroku.api.connection;

import com.google.inject.Inject;
import com.heroku.api.ConnectionTestModule;
import com.heroku.api.exception.HerokuAPIException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Integration tests for authenticating with the Heroku API.
 * @author Naaman Newbold
 */
@Guice(modules = ConnectionTestModule.class)
public class ConnectionIntegrationTest {

    @Inject
    ConnectionTestModule.AuthenticationTestCredentials cred;

    @Test(groups = "integration")
    public void testValidUsernameAndPassword() throws IOException {
        ConnectionProvider connectionProvider = new BasicAuthConnectionProvider(cred.username, cred.password);
        Connection conn = connectionProvider.getConnection();
        Assert.assertNotNull(conn.getApiKey(), "Expected an API key from login, but it doesn't exist.");
    }

    @DataProvider
    public Object[][] invalidUsernamesAndPasswords() {
        return new Object[][] {
                { null, null },
                { "", ""},
                { "rodneyMullen@powell.peralta.bones.brigade", "fakeUsernameAndPassword"}
        };
    }

    @Test(groups = "integration",
          dataProvider = "invalidUsernamesAndPasswords",
          expectedExceptions = HerokuAPIException.class,
          expectedExceptionsMessageRegExp = "Invalid username and password combination.")
    public void testInvalidUsernameAndPassword(String username, String password) throws IOException {
        ConnectionProvider auth = new BasicAuthConnectionProvider(username, password);
        Connection conn = auth.getConnection();
    }
}
