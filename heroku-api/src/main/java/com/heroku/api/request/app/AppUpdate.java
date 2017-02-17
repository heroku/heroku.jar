package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * TODO: Javadoc
 *
 * @author Joe Kutner
 */
public class AppUpdate implements Request<App> {

  private final RequestConfig config;

  public AppUpdate(String appName, Boolean maintenance) {
    this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.AppMaintenance, maintenance.toString());
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.PATCH;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.App.format(config.getAppName());
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
  public App getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.OK.statusCode)
      return parse(in, getClass());
    else
      throw new RequestFailedException("App update failed", code, in);
  }
}