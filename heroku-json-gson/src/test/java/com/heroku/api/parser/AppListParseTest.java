package com.heroku.api.parser;

import com.heroku.api.App;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author mh
 * @since 04.01.12
 */
public class AppListParseTest {

    private static final Type APP_LIST_TYPE = new TypeReference<List<App>>() {
    }.getType();

    @Test
    public void testParseAppWithDomainList() throws UnsupportedEncodingException {
        String appListWithConfiguredDomain="[{\"name\":\"app\",\"stack\":\"bamboo-ree-1.8.7\",\"slug_size\":2383872,\"requested_stack\":null,\"created_at\":\"2010/06/01 07:48:24 -0700\",\"web_url\":\"http://app.heroku.com/\",\"owner_email\":\"test@heroku.com\",\"create_status\":\"complete\",\"id\":200120," +
                "\"domain_name\":{\"created_at\":\"2010/06/03 19:25:08 -0700\",\"updated_at\":\"2010/06/03 19:25:08 -0700\",\"default\":null,\"domain\":\"herokuapp.com\",\"id\":1234,\"app_id\":200120,\"base_domain\":\"herokuapp.com\"}," +
                "\"repo_size\":630784,\"git_url\":\"git@heroku.com:dropphotos.git\",\"repo_migrate_status\":\"complete\",\"dynos\":1,\"workers\":0}" +
                "]";

        final List<App> appList = Json.getJsonParser().parse(appListWithConfiguredDomain.getBytes("UTF-8"), APP_LIST_TYPE);
        Assert.assertEquals(appList.size(),1);
        final App app = appList.get(0);
        Assert.assertEquals(app.getName(),"app");
        Assert.assertEquals(app.getDomain().getDomain(),"herokuapp.com");
        Assert.assertEquals(app.getDomain().getDefault(),null);
        Assert.assertEquals(app.getDynos(),1);
    }

    @Test
    public void testParseAppWithoutDomainList() throws UnsupportedEncodingException {
        String appListWithConfiguredDomain="[{\"name\":\"app\",\"stack\":\"bamboo-ree-1.8.7\",\"slug_size\":2383872,\"requested_stack\":null,\"created_at\":\"2010/06/01 07:48:24 -0700\",\"web_url\":\"http://app.heroku.com/\",\"owner_email\":\"test@heroku.com\",\"create_status\":\"complete\",\"id\":200120," +
                "\"domain_name\":null," +
                "\"repo_size\":630784,\"git_url\":\"git@heroku.com:dropphotos.git\",\"repo_migrate_status\":\"complete\",\"dynos\":1,\"workers\":0}" +
                "]";

        final List<App> appList = Json.getJsonParser().parse(appListWithConfiguredDomain.getBytes("UTF-8"), APP_LIST_TYPE);
        Assert.assertEquals(appList.size(),1);
        final App app = appList.get(0);
        Assert.assertEquals(app.getName(),"app");
        Assert.assertEquals(app.getDomain(),null);
        Assert.assertEquals(app.getDynos(),1);
    }
}
