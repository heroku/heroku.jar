package com.heroku.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@XmlRootElement
public class Collaborator {
    @XmlElement String access;
    @XmlElement String email;

    public String getAccess() {
        return access;
    }

    private void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
