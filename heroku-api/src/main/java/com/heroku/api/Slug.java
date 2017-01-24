package com.heroku.api;

import java.io.Serializable;

/**
 * @author Joe Kutner on 1/24/17.
 */
public class Slug {
  private static final long serialVersionUID = 1L;

  String id;
  Slug.Blob blob;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Blob getBlob() {
    return blob;
  }

  public void setBlob(Blob blob) {
    this.blob = blob;
  }

  public static class Blob implements Serializable {

    private static final long serialVersionUID = 1L;

    String method;
    String url;

    public Blob() {

    }

    public String getMethod() {
      return method;
    }

    public void setId(String method) {
      this.method = method;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
