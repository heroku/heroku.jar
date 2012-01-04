package com.heroku.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Javadoc
 *
 * @author Michael Hunger
 */
/*
"domain_name":{"created_at":"2010/06/03 19:25:08 -0700","updated_at":"2010/06/03 19:25:08 -0700","default":null,"domain":"dropphotos.com","id":27324,"app_id":200391,"base_domain":"dropphotos.com"}
*/
@XmlRootElement(name = "domain_name")
public class Domain {
    @XmlElement String created_at;
    @XmlElement String updated_at;
    @XmlElement(name = "default") String default_name;
    @XmlElement String domain;
    @XmlElement String id;
    @XmlElement String app_id;
    @XmlElement String base_domain;

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getDefault() {
        return default_name;
    }

    public String getDomain() {
        return domain;
    }

    public String getId() {
        return id;
    }

    public String getAppId() {
        return app_id;
    }

    public String getBaseDomain() {
        return base_domain;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private void setDefault_name(String default_name) {
        this.default_name = default_name;
    }

    private void setDomain(String domain) {
        this.domain = domain;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    private void setBase_domain(String base_domain) {
        this.base_domain = base_domain;
    }
}
