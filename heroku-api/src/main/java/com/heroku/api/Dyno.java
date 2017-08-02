package com.heroku.api;

import java.io.Serializable;

/**
 * @author Joe Kutner on 8/2/17.
 *         Twitter: @codefinger
 */
public class Dyno implements Serializable {

  private static final long serialVersionUID = 1L;

  App app;
  String id;
  String name;
  String size;
  String state;
  String type;
  Release release;
  String attach_url;
  String command;
  String updated_at;

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Release getRelease() {
    return release;
  }

  public void setRelease(Release release) {
    this.release = release;
  }

  public String getAttach_url() {
    return attach_url;
  }

  public void setAttach_url(String attach_url) {
    this.attach_url = attach_url;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }
}
