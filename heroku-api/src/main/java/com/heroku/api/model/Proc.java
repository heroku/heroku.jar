package com.heroku.api.model;

import java.net.URL;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Proc {
    String upid;
    String process;
    String type;
    String command;
    String app_name;
    String slug;
    String action;
    String state;
    String pretty_state;
    String transitioned_at;
    int elapsed;
    boolean attached;
    URL rendezvous_url;

    public String getUpid() {
        return upid;
    }

    public void setUpid(String upid) {
        this.upid = upid;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPretty_state() {
        return pretty_state;
    }

    public void setPretty_state(String pretty_state) {
        this.pretty_state = pretty_state;
    }

    public String getTransitioned_at() {
        return transitioned_at;
    }

    public void setTransitioned_at(String transitioned_at) {
        this.transitioned_at = transitioned_at;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public URL getRendezvous_url() {
        return rendezvous_url;
    }

    public void setRendezvous_url(URL rendezvous_url) {
        this.rendezvous_url = rendezvous_url;
    }
}
