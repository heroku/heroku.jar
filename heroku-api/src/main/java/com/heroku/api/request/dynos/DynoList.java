package com.heroku.api.request.dynos;

import com.heroku.api.Dyno;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.util.Range;

import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * @author Joe Kutner on 8/2/17.
 *         Twitter: @codefinger
 */
public class DynoList implements Request<Range<Dyno>> {

  private final String appName;

  private Map<String,String> headers = new HashMap<>();

  public DynoList(String appName, String range) {
    this(appName);
    this.headers.put("Range", range);
  }

  public DynoList(String appName) {
    this.appName = appName;
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
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
    return headers;
  }

  @Override
  public Range<Dyno> getResponse(byte[] in, int code, Map<String, String> responseHeaders) {
    if (code == 200) {
      return Json.parse(in, DynoList.class);
    } else if (code == 206) {
      Range<Dyno> r = Json.parse(in, DynoList.class);
      r.setNextRange(responseHeaders.get("Next-Range"));
      return r;
    } else {
      throw new RequestFailedException("Dyno list failed", code, in);
    }
  }
}
