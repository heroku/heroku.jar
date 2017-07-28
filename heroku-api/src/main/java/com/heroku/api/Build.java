package com.heroku.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class Build implements Serializable {

  private static final long serialVersionUID = 1L;

  SourceBlob source_blob;
  List<Buildpack> buildpacks;
  String output_stream_url;
  String status;
  String id;

  public Build() {}

  public Build(String url, String version, String[] buildpackUrls) {
    this.source_blob = new SourceBlob();
    this.source_blob.setUrl(url);
    this.source_blob.setVersion(version);

    this.buildpacks = new ArrayList<>(buildpackUrls.length);
    for (String buildpackUrl : buildpackUrls) {
      Buildpack b = new Buildpack();
      b.setUrl(buildpackUrl);
      this.buildpacks.add(b);
    }
  }

  public SourceBlob getSource_blob() {
    return source_blob;
  }

  public void setSource_blob(SourceBlob source_blob) {
    this.source_blob = source_blob;
  }

  public List<Buildpack> getBuildpacks() {
    return buildpacks;
  }

  public void setBuildpacks(List<Buildpack> buildpacks) {
    this.buildpacks = buildpacks;
  }

  public String getOutput_stream_url() {
    return output_stream_url;
  }

  public void setOutput_stream_url(String output_stream_url) {
    this.output_stream_url = output_stream_url;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public static class Buildpack implements Serializable {
    String url;

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }

  public static class SourceBlob implements Serializable {

    private static final long serialVersionUID = 1L;

    String checksum;
    String url;
    String version;

    public String getChecksum() {
      return checksum;
    }

    public void setChecksum(String checksum) {
      this.checksum = checksum;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

  }

}
