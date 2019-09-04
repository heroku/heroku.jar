package com.heroku.api.parser;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.ParseException;
import com.heroku.api.request.app.AppInfo;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import static com.heroku.api.parser.Json.parse;
import static com.heroku.api.parser.Json.encode;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author mh
 * @since 04.01.12
 */
public class JsonParseTest {

    private static final Type APP_LIST_TYPE = new TypeReference<List<App>>() {
    }.getType();

    @DataProvider
    Object[][] getParsers() {
        return new Object[][]{{new GsonParser()}, {new JacksonParser()}};
    }

    @Test(dataProvider = "getParsers")
    public void testParseAppWithDomainList(Parser parser) throws UnsupportedEncodingException {
        String appListWithConfiguredDomain = "[{\"name\":\"app\",\"stack\":{\"name\":\"cedar-14\"},\"slug_size\":2383872,\"requested_stack\":null,\"created_at\":\"2010/06/01 07:48:24 -0700\",\"web_url\":\"http://app.heroku.com/\",\"owner_email\":\"test@heroku.com\",\"create_status\":\"complete\",\"id\":200120," +
                "\"domain_name\":{\"created_at\":\"2010/06/03 19:25:08 -0700\",\"updated_at\":\"2010/06/03 19:25:08 -0700\",\"status\":\"pending\",\"hostname\":\"herokuapp.com\",\"id\":1234}," +
                "\"repo_size\":630784,\"git_url\":\"git@heroku.com:dropphotos.git\",\"maintenance\":\"false\"}" +
                "]";

        final List<App> appList = parser.parse(appListWithConfiguredDomain.getBytes("UTF-8"), APP_LIST_TYPE);
        Assert.assertEquals(appList.size(), 1);
        final App app = appList.get(0);
        Assert.assertEquals(app.getName(), "app");
        Assert.assertEquals(app.getDomain().getHostname(), "herokuapp.com");
        Assert.assertEquals(app.getDomain().getStatus(), "pending");
    }

    @Test(dataProvider = "getParsers")
    public void testParseAppWithoutDomainList(Parser parser) throws UnsupportedEncodingException {
        String appListWithConfiguredDomain = "[{\"name\":\"app\",\"stack\":{\"name\":\"cedar-14\"},\"slug_size\":2383872,\"requested_stack\":null,\"created_at\":\"2010/06/01 07:48:24 -0700\",\"web_url\":\"http://app.heroku.com/\",\"owner_email\":\"test@heroku.com\",\"create_status\":\"complete\",\"id\":200120," +
                "\"domain_name\":null," +
                "\"repo_size\":630784,\"git_url\":\"git@heroku.com:dropphotos.git\",\"maintenance\":\"false\"}" +
                "]";

        final List<App> appList = parser.parse(appListWithConfiguredDomain.getBytes("UTF-8"), APP_LIST_TYPE);
        Assert.assertEquals(appList.size(), 1);
        final App app = appList.get(0);
        Assert.assertEquals(app.getName(), "app");
        Assert.assertEquals(app.getDomain(), null);
    }

    @Test(dataProvider = "getParsers")
    public void unknownPropertiesShouldNotThrowExceptionsDuringParsing(Parser parser) throws UnsupportedEncodingException {
        String unknownProperty = "{\"unknown_property\":\"this property doesn't exist\"}";
        final App appList = parser.parse(unknownProperty.getBytes("UTF-8"), App.class);
        Assert.assertNull(appList.getName());
    }
    
    @Test(expectedExceptions = ParseException.class)
    public void nullRequestTypeShouldThrowParseException() {
        parse(new byte[]{}, null);
    }
    
    @Test
    public void genericTypeFromRequestInheritanceShouldParse() {
        App app = parse("{\"name\":\"test\"}".getBytes(), SubAppInfo.class);
        assertEquals(app.getName(), "test");
    }

    @Test
    public void genericTypeFromMultipleLevelsOfRequestInheritanceShouldParse() {
        App app = parse("{\"name\":\"test\"}".getBytes(), SubSubAppInfo.class);
        assertEquals(app.getName(), "test");
    }

    @Test(expectedExceptions = ParseException.class)
    public void invalidJSONShouldThrowParseException() {
        parse("{{".getBytes(), AppInfo.class);
    }

    @Test
    public void testEncodeApp() {
        App a = new App().on(Heroku.Stack.Heroku18);
        a.setMaintenance(true);
        String json = encode(a);
        assertTrue(json.indexOf("true") != 0);
    }

    public static class SubAppInfo extends AppInfo {
        public SubAppInfo(String appName) {
            super(appName);
        }
    }

    public static class SubSubAppInfo extends SubAppInfo {
        public SubSubAppInfo(String appName) {
            super(appName);
        }
    }
}
