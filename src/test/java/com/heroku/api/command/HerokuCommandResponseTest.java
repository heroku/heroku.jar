package com.heroku.api.command;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuCommandResponseTest {
    @Test
    public void testArrayOfJsonElementsMapsToHerokuCommandMap() {
        String jsonArray = "[{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"},{\"name\":\"vivid-summer-2426\",\"stack\":\"cedar\"}]";
        HerokuCommandListMapResponse res = new HerokuCommandListMapResponse(jsonArray.getBytes(), true);
        Assert.assertNotNull(res.get("warm-frost-3139"));
    }

    @Test
    public void testSingleJsonElementMapsToHerokuCommandMap() {
        String json = "{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"}";
        HerokuCommandMapResponse res = new HerokuCommandMapResponse(json.getBytes(), true);
        Assert.assertEquals(res.get("name"), "warm-frost-3139");
    }
}
