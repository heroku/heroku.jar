package com.heroku.api;

import java.io.Serializable;

public class Team implements Serializable {

  private static final long serialVersionUID = 1L;

  String id;
  String name;

  public Team() {}

  public Team(String name) {
    this.name = name;
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
}
