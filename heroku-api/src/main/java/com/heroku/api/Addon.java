package com.heroku.api;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Addon implements Serializable {

    private static final long serialVersionUID = 1L;

    String name;
    String description;
    URL url;
    String state;
    String id;
    Plan plan;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    private void setUrl(URL url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }

    public static class Plan implements Serializable {

        private static final long serialVersionUID = 1L;

        String id;

        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}