package com.heroku.api.parser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
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
            return (T) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(data));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
