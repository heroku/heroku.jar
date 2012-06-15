package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonChange implements Serializable {

    private static final long serialVersionUID = 1L;

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
