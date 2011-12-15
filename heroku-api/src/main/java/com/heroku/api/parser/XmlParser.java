package com.heroku.api.parser;

import com.heroku.api.App;
import com.heroku.api.Collaborator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
            JAXBContext jaxbContext = JAXBContext.newInstance((Class)type);
            return (T)jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(data));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
