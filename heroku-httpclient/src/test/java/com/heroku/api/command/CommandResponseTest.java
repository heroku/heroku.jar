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
public class CommandResponseTest {
    @Test
    public void testArrayOfJsonElementsMapsToHerokuCommandMap() {
        String jsonArray = "[{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"},{\"name\":\"vivid-summer-2426\",\"stack\":\"cedar\"}]";
        JsonArrayResponse res = new JsonArrayResponse(CommandUtil.bytesInputStream(jsonArray.getBytes()));
        Assert.assertNotNull(res.get("warm-frost-3139"));
    }

    @Test
    public void testSingleJsonElementMapsToHerokuCommandMap() {
        String json = "{\"name\":\"warm-frost-3139\",\"stack\":\"cedar\"}";
        JsonMapResponse res = new JsonMapResponse(CommandUtil.bytesInputStream(json.getBytes()));
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
        XmlMapResponse response = new XmlMapResponse(CommandUtil.bytesInputStream(xml.getBytes()));
        Assert.assertEquals(response.get("name"), "fierce-rain-7019");
    }
}
