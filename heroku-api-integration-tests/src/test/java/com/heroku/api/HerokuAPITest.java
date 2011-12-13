package com.heroku.api;

import com.heroku.api.connection.HttpClientConnection;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static com.heroku.api.IntegrationTestConfig.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@Guice(moduleFactory = TestModuleFactory.class)
public class HerokuAPITest {
    
    @DataProvider
    public Object[][] validConfigurations() {
        return new Object[][] {
                { new HerokuAPIConfig().setUsername(USER.getRequiredConfig()).setPassword(PASSWORD.getRequiredConfig()) },
                { new HerokuAPIConfig().setApiKey(APIKEY.getRequiredConfig()) }
        };
    }
    
    @Test(dataProvider = "validConfigurations")
    public void testValidHerokuAPIConfiguration(HerokuAPIConfig config) {
        HerokuAPI api = new HerokuAPI(config);
        assertTrue(api.connection instanceof HttpClientConnection);
        assertNotNull(APIKEY.getRequiredConfig(), api.connection.getApiKey());
    }
    
}
