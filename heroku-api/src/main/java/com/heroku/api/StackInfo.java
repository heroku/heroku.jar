package com.heroku.api;

import java.io.Serializable;

/**
 * @author Naaman Newbold
 */
public class StackInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    String requested;
    boolean beta;
    boolean current;
    String name;

    public String getRequested() {
        return requested;
    }

    private void setRequested(String requested) {
        this.requested = requested;
    }

    public boolean isBeta() {
        return beta;
    }

    private void setBeta(boolean beta) {
        this.beta = beta;
    }

    public boolean isCurrent() {
        return current;
    }

    private void setCurrent(boolean current) {
        this.current = current;
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
