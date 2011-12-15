package com.heroku.api.parser;

import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class XmlParserTest {
    String xmlList = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<collaborators>" +
            "  <collaborator>" +
            "    <email>java123@test.heroku.com</email>" +
            "    <access>edit</access>" +
            "  </collaborator>" +
            "  <collaborator>" +
            "    <email>java456@test.heroku.com</email>" +
            "    <access>edit</access>" +
            "  </collaborator>" +
            "  <collaborator>" +
            "    <email>java789@test.heroku.com</email>" +
            "    <access>edit</access>" +
            "  </collaborator>" +
            "</collaborators>";

    @XmlRootElement
    public static class Collaborators {
//        @XmlElement(name = "collaborator")
        List<Collaborator> collaborators;

        public List<Collaborator> getCollaborator() {
            return collaborators;
        }

        public void setCollaborator(List<Collaborator> collablist) {
            this.collaborators = collablist;
        }


    }

    @XmlRootElement
    public static class Collaborator {
        String email;
        String access;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }
    }

    @Test
    public void testXmlListParser() {
        XmlParser parser = new XmlParser();
        Collaborators c = parser.parse(xmlList.getBytes(), Collaborators.class);
        c.getCollaborator();
    }
}
