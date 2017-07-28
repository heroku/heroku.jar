package com.heroku.api.request.builds;

import com.heroku.api.BuildResult;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class BuildResultInfo implements Request<BuildResult> {

  private final RequestConfig config;

  private String buildId;

  public BuildResultInfo(String appName, String buildId) {
    this.buildId = buildId;
    this.config = new RequestConfig().app(appName);
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.BuildResult.format(config.getAppName(), buildId);
  }

  @Override
  public boolean hasBody() {
    return false;
  }

  @Override
  public String getBody() {
    throw noBody();
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
    return Collections.emptyMap();
  }

  @Override
  public BuildResult getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
    if (status == Http.Status.OK.statusCode) {
      return parse(bytes, getClass());
    } else {
      throw new RequestFailedException("Unable to get build result info.", status, bytes);
    }
  }
}
