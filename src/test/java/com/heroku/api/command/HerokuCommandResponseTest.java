package com.heroku.api.command;

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
public class HerokuCommandResponseTest {
    @Test
    public void testArrayOfJsonElementsMapsToHerokuCommandMap() {
        String jsonArray = "[{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"},{\"name\":\"vivid-summer-2426\",\"stack\":\"cedar\"}]";
        HerokuCommandJsonListMapResponse res = new HerokuCommandJsonListMapResponse(jsonArray.getBytes(), true);
        Assert.assertNotNull(res.get("warm-frost-3139"));
    }

    @Test
    public void testSingleJsonElementMapsToHerokuCommandMap() {
        String json = "{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"}";
        HerokuCommandJsonMapResponse res = new HerokuCommandJsonMapResponse(json.getBytes(), true);
        Assert.assertEquals(res.get("name"), "warm-frost-3139");
    }

    @Test
    public void testSingleXmlElementMapsToHerokuXmlCommandMap() throws SAXException, ParserConfigurationException, IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<app>\n" +
                "  <created-at type=\"datetime\">2011-11-02T17:02:16-07:00</created-at>\n" +
                "  <name>fierce-rain-7019</name>\n" +
                "  <repo-size type=\"integer\" nil=\"true\"></repo-size>\n" +
                "  <id>app1681795@heroku.com</id>\n" +
                "</app>";
        HerokuCommandXmlMapResponse response = new HerokuCommandXmlMapResponse(xml.getBytes(), true);
        Assert.assertEquals(response.get("name"), "fierce-rain-7019");
    }
}
