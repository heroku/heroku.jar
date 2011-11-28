package com.heroku.api.request.response;

import com.heroku.api.request.Response;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.request.response.XmlUtil.parse;


public class XmlMapResponse extends DefaultHandler implements Response {

    private final byte[] rawData;
    private final HashMap<String, String> data = new HashMap<String, String>();
    private String lastKey;
    private StringBuffer charBuffer;

    public XmlMapResponse(byte[] bytes) {
        this.rawData = bytes;
        parse(this.rawData, this);
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
    public String get(String key) {
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
