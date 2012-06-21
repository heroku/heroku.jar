package com.heroku.api;

import java.io.Serializable;
import java.net.URL;

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
    String beta;
    int price_cents;
    String price_unit;
    String id;
    Boolean configured;

    public Boolean getConfigured() {
        return configured;
    }

    private void setConfigured(Boolean configured) {
        this.configured = configured;
    }

    public int getPriceCents() {
        return price_cents;
    }

    private void setPrice_cents(int price_cents) {
        this.price_cents = price_cents;
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getPriceUnit() {
        return price_unit;
    }


    private void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
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

    public String getBeta() {
        return beta;
    }

    private void setBeta(String beta) {
        this.beta = beta;
    }

}
