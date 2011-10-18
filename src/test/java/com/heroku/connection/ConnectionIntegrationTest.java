package com.heroku.connection;

import com.google.inject.Inject;
import org.testng.Assert;
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

    @Test
    public void testValidUsernameAndPassword() throws IOException {
        HerokuConnectionConfig auth = HerokuConnectionConfig.newInstance(cred.username, cred.password);
        HerokuConnection conn = auth.connect();
        Assert.assertNotNull(conn.getApiKey(), "Expected an API key from login, but it doesn't exist.");
    }
}
