package com.heroku.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Release implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    int version;
    String status;
    Map<String,String> user;
    String description;
    String created_at;
    List<String> addon_plan_names;
    Slug slug;

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return created_at;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<String> getAddon_plan_names() {
        return addon_plan_names;
    }

    private void setAddon_plan_names(List<String> addon_plan_names) {
        this.addon_plan_names = addon_plan_names;
    }

    public Slug getSlug() {
        return slug;
    }

    public void setSlug(Slug slug) {
        this.slug = slug;
    }
}
