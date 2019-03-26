package com.heroku.api;

import java.io.Serializable;

public class Team implements Serializable {

  private static final long serialVersionUID = 1L;

  String name;

  public Team(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
