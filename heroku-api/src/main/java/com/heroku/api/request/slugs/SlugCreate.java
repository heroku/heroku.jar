package com.heroku.api.request.slugs;

import com.heroku.api.Heroku;
import com.heroku.api.Slug;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 1/24/17.
 *         Twitter: @codefinger
 */
public class SlugCreate implements Request<Slug> {

  private final RequestConfig config;

  private final Map<String, String> processTypes;

  public SlugCreate(String appName, Map<String, String> processTypes) {
    this.processTypes = processTypes;
    this.config = new RequestConfig().app(appName);
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.POST;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Slugs.format(config.getAppName());
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
    Map<String,Object> bodyMap = config.asMap();
    bodyMap.put(Heroku.RequestKey.ProcessTypes.queryParameter, processTypes);
    return bodyMap;
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
  public Slug getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.CREATED.statusCode)
      return parse(in, getClass());
    else if (code == Http.Status.ACCEPTED.statusCode)
      return parse(in, getClass());
    else
      throw new RequestFailedException("Failed to create slug", code, in);
  }
}
