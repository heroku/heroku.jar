package com.heroku.api;

import java.io.Serializable;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AddonChange implements Serializable {

    private static final long serialVersionUID = 1L;

    String state;

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }
}
