package com.heroku.api.model;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonChange {
    String status;
    String message;
    String price;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
