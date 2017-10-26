package com.heroku.api.request.buildpacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

/**
 * @author Joe Kutner on 10/26/17.
 *         Twitter: @codefinger
 */
public class BuildpackInstallationUpdate implements Request<Unit> {

  private final RequestConfig config;

  private final Map<String, List<Map<String,String>>> buildpacks;

  public BuildpackInstallationUpdate(String appName, List<String> buildpacks) {
    List<Map<String,String>> buildpackUpdatesList = new ArrayList<>();

    for (String b : buildpacks) {
      Map<String,String> buildpackUpdate = new HashMap<>();
      buildpackUpdate.put("buildpack", b);
      buildpackUpdatesList.add(buildpackUpdate);
    }

    this.buildpacks = new HashMap<>();
    this.buildpacks.put("updates", buildpackUpdatesList);

    this.config = new RequestConfig().app(appName);
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.PUT;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.BuildpackInstalltions.format(config.getAppName());
  }

  @Override
  public boolean hasBody() {
    return true;
  }

  @Override
  public String getBody() {
    return Json.encode(buildpacks);
  }

  @Override
  public Map<String, ?> getBodyAsMap() {
    return buildpacks;
  }

  @Override
  public Http.Accept getResponseType() {
    return Http.Accept.JSON;
  }

  @Override
  public Map<String, String> getHeaders() {
    return Collections.emptyMap();
  }

  @Override
  public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.OK.statusCode)
      return Unit.unit;
    else
      throw new RequestFailedException("Buildpack Installations update failed", code, in);
  }
}
