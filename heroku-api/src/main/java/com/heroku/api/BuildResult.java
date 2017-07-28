package com.heroku.api;

import java.io.Serializable;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class BuildResult implements Serializable {

  private static final long serialVersionUID = 1L;

  Build build;
  String exit_code;

  public Build getBuild() {
    return build;
  }

  public void setBuild(Build build) {
    this.build = build;
  }

  public String getExit_code() {
    return exit_code;
  }

  public void setExit_code(String exit_code) {
    this.exit_code = exit_code;
  }
}
