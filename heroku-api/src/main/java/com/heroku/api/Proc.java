package com.heroku.api;

import java.io.Serializable;
import java.net.URI;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Proc implements Serializable {

    private static final long serialVersionUID = 1L;

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
    URI rendezvous_url;

    public String getUpid() {
        return upid;
    }

    private void setUpid(String upid) {
        this.upid = upid;
    }

    public String getProcess() {
        return process;
    }

    private void setProcess(String process) {
        this.process = process;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    private void setCommand(String command) {
        this.command = command;
    }

    public String getAppName() {
        return app_name;
    }

    private void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getSlug() {
        return slug;
    }

    private void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAction() {
        return action;
    }

    private void setAction(String action) {
        this.action = action;
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }

    public String getPrettyState() {
        return pretty_state;
    }

    private void setPretty_state(String pretty_state) {
        this.pretty_state = pretty_state;
    }

    public String getTransitionedAt() {
        return transitioned_at;
    }

    private void setTransitioned_at(String transitioned_at) {
        this.transitioned_at = transitioned_at;
    }

    public int getElapsed() {
        return elapsed;
    }

    private void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public boolean isAttached() {
        return attached;
    }

    private void setAttached(boolean attached) {
        this.attached = attached;
    }

    public URI getRendezvousUrl() {
        return rendezvous_url;
    }

    private void setRendezvous_url(URI rendezvous_url) {
        this.rendezvous_url = rendezvous_url;
    }
}
