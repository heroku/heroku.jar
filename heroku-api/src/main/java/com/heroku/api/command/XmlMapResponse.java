package com.heroku.api.command;

import com.heroku.api.exception.HerokuAPIException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class XmlMapResponse extends DefaultHandler implements CommandResponse {

    private final byte[] rawData;
    private final boolean success;
    private volatile boolean rawDataIsProcessed = false;
    private Object lock = new Object();
    private final HashMap<String, String> data = new HashMap<String, String>();
    private String lastKey;
    private StringBuffer charBuffer;

    public XmlMapResponse(byte[] data, boolean success) {
        this.rawData = data;
        this.success = success;
    }

    @Override
    public void startElement(String namespace, String simpleName, String qualifiedName, Attributes attributes) throws SAXException {
        // only supports single level xml documents. overwriting the previous element name and data is ok in this case.
        lastKey = qualifiedName;
        data.put(lastKey, null);
        charBuffer = null;
    }

    @Override
    public void endElement(String s, String s1, String s2) throws SAXException {
        if (lastKey != null && data.containsKey(lastKey) && charBuffer != null) {
            data.put(lastKey, charBuffer.toString().trim());
            charBuffer = null;
            lastKey = null;
        }
    }

    @Override
    public void characters(char[] chars, int offset, int offsetLen) throws SAXException {
        if (charBuffer == null)
            charBuffer = new StringBuffer();

        charBuffer.append(chars, offset, offsetLen);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String get(String key) {
        if (!rawDataIsProcessed) {
            synchronized (lock) {
                if (!rawDataIsProcessed) {
                    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                    try {
                        SAXParser parser = parserFactory.newSAXParser();
                        parser.parse(new ByteArrayInputStream(rawData), this);
                    } catch (Exception e) {
                        throw new HerokuAPIException("Unable to parse XML response.", e);
                    }
                    rawDataIsProcessed = true;
                }
            }
        }
        return data.get(key);
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }

    public Map<String, String> getData() {
        return new HashMap<String, String>(data);
    }
}
