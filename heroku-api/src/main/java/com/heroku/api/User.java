package com.heroku.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@XmlRootElement
public class User {

    @XmlElement String created_at;
    @XmlElement String last_login;
    @XmlElement String id;
    @XmlElement String email;

    public String getCreatedAt() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLastLogin() {
        return last_login;
    }

    private void setLast_Login(String last_login) {
        this.last_login = last_login;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
