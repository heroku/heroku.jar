package com.heroku.api;

import java.io.Serializable;

/**
 * @author Joe Kutner on 1/26/17.
 */
public class LogSession implements Serializable {

  private static final long serialVersionUID = 1L;

  String id;
  String logplex_url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogplex_url() {
    return logplex_url;
  }

  public void setLogplex_url(String logplex_url) {
    this.logplex_url = logplex_url;
  }
}
