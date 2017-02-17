package com.heroku.api.request.slugs;

import com.heroku.api.Heroku;
import com.heroku.api.Slug;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 1/24/17.
 *         Twitter: @codefinger
 */
public class SlugInfo implements Request<Slug> {

  private final RequestConfig config;

  public SlugInfo(String appName, String slugId) {
    this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.Slug, slugId);
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Slug.format(config.getAppName(), config.get(Heroku.RequestKey.Slug));
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
  public Slug getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
    if (status == Http.Status.OK.statusCode) {
      return parse(bytes, getClass());
    } else {
      throw new RequestFailedException("Unable to get slug info.", status, bytes);
    }
  }
}