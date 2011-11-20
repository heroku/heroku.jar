package com.heroku.api.command;

import com.heroku.api.command.response.JsonArrayResponse;
import com.heroku.api.command.response.JsonMapResponse;
import com.heroku.api.command.response.XmlMapResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class JsonResponseTest {
    @Test
    public void testArrayOfJsonElementsMapsToHerokuCommandMap() {
        String jsonArray = "[{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"},{\"name\":\"vivid-summer-2426\",\"stack\":\"cedar\"}]";
        JsonArrayResponse res = new JsonArrayResponse(jsonArray.getBytes());
        Assert.assertNotNull(res.get("warm-frost-3139"));
    }

    @Test
    public void testSingleJsonElementMapsToHerokuCommandMap() {
        String json = "{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"}";
        JsonMapResponse res = new JsonMapResponse(json.getBytes());
        Assert.assertEquals(res.get("name"), "warm-frost-3139");
    }


}
