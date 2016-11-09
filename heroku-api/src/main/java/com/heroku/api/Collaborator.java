package com.heroku.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Collaborator implements Serializable {

    private static final long serialVersionUID = 1L;

    List<Map<String,String>> permissions;
    Map<String,String> user;
    String role;

    public List<Map<String, String>> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Map<String, String>> permissions) {
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return user.get("email");
    }

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }
}
