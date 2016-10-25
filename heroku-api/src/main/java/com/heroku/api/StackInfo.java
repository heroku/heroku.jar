package com.heroku.api;

import java.io.Serializable;

/**
 * @author Naaman Newbold
 */
public class StackInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    String name;
    String state;

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Heroku.Stack getStack() {
        return Heroku.Stack.fromString(name);
    }

}
