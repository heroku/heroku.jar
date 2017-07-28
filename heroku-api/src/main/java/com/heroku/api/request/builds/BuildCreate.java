package com.heroku.api.request.builds;

import com.heroku.api.Build;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.*;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class BuildCreate implements Request<Build> {

  private final RequestConfig config;

  private List<Map<String,String>> buildpacks = new ArrayList<>();

  public BuildCreate(String appName, Build build) {
    Map<Heroku.RequestKey, RequestConfig.Either> sourceBlob = new HashMap<>();
    sourceBlob.put(Heroku.RequestKey.SourceBlobUrl, new RequestConfig.Either(build.getSource_blob().getUrl()));
    sourceBlob.put(Heroku.RequestKey.SourceBlobVersion, new RequestConfig.Either(build.getSource_blob().getVersion()));
    if (null != build.getSource_blob().getChecksum()) sourceBlob.put(Heroku.RequestKey.SourceBlobChecksum, new RequestConfig.Either(build.getSource_blob().getChecksum()));

    RequestConfig builder = new RequestConfig();
    builder = builder.with(Heroku.RequestKey.SourceBlob, sourceBlob);

    if (build.getBuildpacks() != null) {
      for (Build.Buildpack b: build.getBuildpacks()) {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put(Heroku.RequestKey.BuildpackUrl.queryParameter, b.getUrl());
        buildpacks.add(urlMap);
      }
    }

    config = builder.app(appName);
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.POST;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Builds.format(config.getAppName());
  }

  @Override
  public boolean hasBody() {
    return true;
  }

  @Override
  public String getBody() {
    return Json.encode(getBodyAsMap());
  }

  @Override
  public Map<String,Object> getBodyAsMap() {
    Map<String, Object> map = config.asMap();
    if (!buildpacks.isEmpty()) {
      map.put(Heroku.RequestKey.Buildpacks.queryParameter, buildpacks);
    }
    return map;
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
  public Build getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.CREATED.statusCode)
      return parse(in, getClass());
    else if (code == Http.Status.ACCEPTED.statusCode)
      return parse(in, getClass());
    else
      throw new RequestFailedException("Failed to create build", code, in);
  }
}
