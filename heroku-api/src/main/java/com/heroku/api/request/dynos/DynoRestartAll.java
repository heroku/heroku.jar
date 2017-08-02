package com.heroku.api.request.dynos;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * @author Joe Kutner on 8/2/17.
 *         Twitter: @codefinger
 */
public class DynoRestartAll implements Request<Unit> {

  private final String appName;

  public DynoRestartAll(String appName) {
    this.appName = appName;
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.DELETE;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Dynos.format(this.appName);
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
    return Collections.emptyMap();
  }

  @Override
  public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.ACCEPTED.statusCode)
      return Unit.unit;
    else
      throw new RequestFailedException("Dyno restart failed", code, in);
  }
}
