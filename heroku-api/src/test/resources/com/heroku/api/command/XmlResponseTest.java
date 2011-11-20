package com.heroku.api.command;

import com.heroku.api.command.response.XmlMapResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class XmlResponseTest {
    @Test
    public void testSingleXmlElementMapsToHerokuXmlCommandMap() throws SAXException, ParserConfigurationException, IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<app>\n" +
                "  <created-at type=\"datetime\">2011-11-02T17:02:16-07:00</created-at>\n" +
                "  <name>fierce-rain-7019</name>\n" +
                "  <repo-size type=\"integer\" nil=\"true\"></repo-size>\n" +
                "  <id>app1681795@heroku.com</id>\n" +
                "</app>";
        XmlMapResponse response = new XmlMapResponse(xml.getBytes());
        Assert.assertEquals(response.get("name"), "fierce-rain-7019");
    }
}
