package com.heroku.api.request.buildpacks;

import static com.heroku.api.http.HttpUtil.noBody;

import java.util.HashMap;
import java.util.Map;

import com.heroku.api.BuildpackInstallation;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.util.Range;

/**
 * @author Joe Kutner on 10/25/17.
 *         Twitter: @codefinger
 */
public class BuildpackInstallationList implements Request<Range<BuildpackInstallation>> {

  private final String appName;

  private Map<String,String> headers = new HashMap<>();

  public BuildpackInstallationList(String appName, String range) {
    this(appName);
    this.headers.put("Range", range);
  }

  public BuildpackInstallationList(String appName) {
    this.appName = appName;
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.BuildpackInstalltions.format(this.appName);
  }

  @Override
  public boolean hasBody() {
    return false;
  }

  @Override
  public String getBody() {
    throw HttpUtil.noBody();
  }

  @Override
  public Map<String,Object> getBodyAsMap() {
    throw noBody();
  }

  @Override
  public Http.Accept getResponseType() {
    return Http.Accept.JSON;
  }

  @Override
  public Map<String, String> getHeaders() {
    return headers;
  }

  @Override
  public Range<BuildpackInstallation> getResponse(byte[] in, int code, Map<String, String> responseHeaders) {
    if (code == 200) {
      return Json.parse(in, BuildpackInstallationList.class);
    } else if (code == 206) {
      Range<BuildpackInstallation> r = Json.parse(in, BuildpackInstallationList.class);
      r.setNextRange(responseHeaders.get("Next-Range"));
      return r;
    } else {
      throw new RequestFailedException("Buildpack Installation list failed", code, in);
    }
  }
}
