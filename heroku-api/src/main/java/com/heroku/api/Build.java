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
  Identifiable release;
  Identifiable slug;
  String status;
  String updated_at;
  String created_at;
  String id;
  User user;

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

  public Identifiable getRelease() {
    return release;
  }

  public void setRelease(Identifiable release) {
    this.release = release;
  }

  public Identifiable getSlug() {
    return slug;
  }

  public void setSlug(Identifiable slug) {
    this.slug = slug;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  public static class Identifiable implements Serializable {
    String id;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

  public static class User extends Identifiable {
    String email;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

}
