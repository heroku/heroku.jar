package com.heroku.api.parser;

import com.heroku.api.App;
import com.heroku.api.Collaborator;
import com.heroku.api.Domain;
import com.heroku.api.User;
import com.heroku.api.exception.ParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class XmlParser implements Parser {

    static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(App.class, Collaborator.class, Domain.class, User.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(byte[] data, Type type) {
        try {
            return (T) jaxbContext.createUnmarshaller().unmarshal(new StringReader(new String(data).trim()));
        } catch (JAXBException e) {
            throw new ParseException(e);
        }
    }

}
