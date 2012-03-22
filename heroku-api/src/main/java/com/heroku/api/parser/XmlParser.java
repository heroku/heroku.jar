package com.heroku.api.parser;

import com.heroku.api.exception.ParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class XmlParser implements Parser {
    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(byte[] data, Type type) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance((Class) type);
            return (T) jaxbContext.createUnmarshaller().unmarshal(new StringReader(new String(data).trim()));
        } catch (JAXBException e) {
            throw new ParseException(e);
        }
    }

}
