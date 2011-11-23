package com.heroku.api.command.response;

import com.heroku.api.exception.HerokuAPIException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class XmlUtil {
    static void parse(byte[] data, DefaultHandler xmlMapResponse) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(new ByteArrayInputStream(data), xmlMapResponse);
        } catch (Exception e) {
            throw new HerokuAPIException("Unable to parse XML response.", e);
        }
    }
}
