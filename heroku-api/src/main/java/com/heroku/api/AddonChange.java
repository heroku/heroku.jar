package com.heroku.api;

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

    private void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getPrice() {
        return price;
    }

    private void setPrice(String price) {
        this.price = price;
    }

}
