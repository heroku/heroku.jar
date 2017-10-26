package com.heroku.api;

/**
 * @author Joe Kutner on 10/25/17.
 *         Twitter: @codefinger
 */
public class BuildpackInstallation {

  private static final long serialVersionUID = 1L;

  int ordinal;
  Buildpack buildpack;

  public int getOrdinal() {
    return ordinal;
  }

  public void setOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  public Buildpack getBuildpack() {
    return buildpack;
  }

  public void setBuildpack(Buildpack buildpack) {
    this.buildpack = buildpack;
  }

  public static class Buildpack {

    private static final long serialVersionUID = 1L;

    String name;
    String url;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
