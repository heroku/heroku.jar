package com.heroku.api.request.response;

import com.heroku.api.request.Response;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

import static com.heroku.api.request.response.XmlUtil.parse;

/**
 * One-dimensional array XML response parser. This implementation ignores the root element,
 * treats all children of the document root as an array item, and any of its children as
 * name-value pairs. If the depth of the document is more than 2, the parser will blow up.
 * (e.g. <root><item><child><child2></child2></child></item></root>)
 *
 * @author Naaman Newbold
 */
public class XmlArrayResponse extends DefaultHandler implements Response {

    private final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private final byte[] rawData;
    private final LinkedList<Object> stack = new LinkedList<Object>();
    private boolean rootSkipped = false;

    public XmlArrayResponse(byte[] rawData) {
        this.rawData = rawData;
        parse(this.rawData, this);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (rootSkipped) {
            if (stack.size() == 0) {
                LinkedHashMap<String, String> items = new LinkedHashMap<String, String>();
                data.add(items);
                stack.add(items);
            } else {
                stack.add(qName);
            }
        }
        else
            rootSkipped = true;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (stack.size() != 0)
            stack.removeLast();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String charData = new String(ch, start, length);
        charData = charData.trim();
        if (stack.size() > 0 && !(stack.getLast() instanceof Map)) {
            ((Map<String, String>)stack.get(stack.size() - 2)).put((String)stack.getLast(), charData);
        }
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }

    @Override
    public List<Map<String, String>> getData() {
        return data;
    }
    
}
