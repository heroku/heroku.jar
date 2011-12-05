package com.heroku.api.model;

import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Release {
    String name, descr,user, commit;
    String created_at;
    Map<String, String> env;
    List<String> addons;
    Map<String, String> pstable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public List<String> getAddons() {
        return addons;
    }

    public void setAddons(List<String> addons) {
        this.addons = addons;
    }

    public Map<String, String> getPstable() {
        return pstable;
    }

    public void setPstable(Map<String, String> pstable) {
        this.pstable = pstable;
    }
}
