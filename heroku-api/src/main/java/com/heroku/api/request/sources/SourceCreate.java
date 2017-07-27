package com.heroku.api.request.sources;

import com.heroku.api.Heroku;
import com.heroku.api.Source;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;

import java.util.Collections;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;
import static com.heroku.api.parser.Json.parse;

/**
 * @author Joe Kutner on 7/27/17.
 *         Twitter: @codefinger
 */
public class SourceCreate implements Request<Source> {

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.POST;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.Sources.value;
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
  public Source getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
    if (code == Http.Status.CREATED.statusCode)
      return parse(in, getClass());
    else if (code == Http.Status.ACCEPTED.statusCode)
      return parse(in, getClass());
    else
      throw new RequestFailedException("Failed to create source", code, in);
  }
}