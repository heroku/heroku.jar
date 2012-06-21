package com.heroku.api;

import java.io.Serializable;

/**
 * @author Ryan Brainard
 */
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    boolean maintenance;

    public boolean isMaintenance() {
        return maintenance;
    }

    private void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }
}
