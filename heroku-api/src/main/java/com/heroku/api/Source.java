package com.heroku.api;

import java.io.Serializable;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class Source implements Serializable {

  private static final long serialVersionUID = 1L;

  Blob source_blob;

  public Blob getSource_blob() {
    return source_blob;
  }

  public void setSource_blob(Blob source_blob) {
    this.source_blob = source_blob;
  }

  public static class Blob {
    String get_url;
    String put_url;

    public String getGet_url() {
      return get_url;
    }

    public void setGet_url(String get_url) {
      this.get_url = get_url;
    }

    public String getPut_url() {
      return put_url;
    }

    public void setPut_url(String put_url) {
      this.put_url = put_url;
    }
  }

}
