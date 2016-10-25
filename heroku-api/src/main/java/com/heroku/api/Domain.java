package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Michael Hunger
 */
/*
"domain_name":{"created_at":"2010/06/03 19:25:08 -0700","updated_at":"2010/06/03 19:25:08 -0700","default":null,"domain":"dropphotos.com","id":27324,"app_id":200391,"base_domain":"dropphotos.com"}
*/
public class Domain implements Serializable {

    private static final long serialVersionUID = 1L;

    String created_at;
    String updated_at;
    String hostname;
    String id;
    String kind;
    String status;
    String cname;

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getHostname() {
        return hostname;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private void setHostname(String domain) {
        this.hostname = domain;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setKind(String kind) {
        this.kind = kind;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
