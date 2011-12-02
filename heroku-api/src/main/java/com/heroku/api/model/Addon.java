package com.heroku.api.model;

import java.net.URL;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Addon {
    String name;
    String description;
    URL url;
    String state;
    String beta;
    int price_cents;
    String price_unit;
    String id;
    Boolean configured;

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
    }

    public int getPrice_cents() {
        return price_cents;
    }

    public void setPrice_cents(int price_cents) {
        this.price_cents = price_cents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice_unit() {
        return price_unit;
    }


    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBeta() {
        return beta;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

}
