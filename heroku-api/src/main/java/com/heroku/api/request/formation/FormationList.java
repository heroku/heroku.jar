package com.heroku.api.request.formation;

import com.heroku.api.Formation;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 8/4/17.
 *         Twitter: @codefinger
 */
public class FormationList implements Request<List<Formation>> {

  private final String appName;

  public FormationList(String appName) {
    this.appName = appName;
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Formations.format(this.appName);
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
  public List<Formation> getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
    if (Http.Status.OK.equals(status)) {
      return parse(bytes, getClass());
    } else {
      throw new RequestFailedException("Unable to list formation info.", status, bytes);
    }
  }

}
