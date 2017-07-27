package com.heroku.api.request.builds;

import com.heroku.api.Build;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class BuildCreate implements Request<Build> {

  private final RequestConfig config;

  public BuildCreate(String appName, Build build) {
    Map<Heroku.RequestKey, RequestConfig.Either> sourceBlob = new HashMap<>();
    sourceBlob.put(Heroku.RequestKey.SourceBlobUrl, new RequestConfig.Either(build.getSource_blob().getUrl()));
    sourceBlob.put(Heroku.RequestKey.SourceBlobVersion, new RequestConfig.Either(build.getSource_blob().getVersion()));
    if (null != build.getSource_blob().getChecksum()) sourceBlob.put(Heroku.RequestKey.SourceBlobChecksum, new RequestConfig.Either(build.getSource_blob().getChecksum()));

    RequestConfig builder = new RequestConfig();
    builder = builder.
        with(Heroku.RequestKey.AppName, appName).
        with(Heroku.RequestKey.SourceBlob, sourceBlob);

    if (build.getBuildpacks() != null) {
      Map<Heroku.RequestKey, RequestConfig.Either> buildpacks = new HashMap<>();
      for (Build.Buildpack b : build.getBuildpacks()) {
        buildpacks.put(Heroku.RequestKey.BuildpackUrl, new RequestConfig.Either(b.getUrl()));
      }
      builder = builder.with(Heroku.RequestKey.Buildpacks, buildpacks);
    }

    config = builder;
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
    return config.asJson();
  }

  @Override
  public Map<String,Object> getBodyAsMap() {
    return config.asMap();
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
