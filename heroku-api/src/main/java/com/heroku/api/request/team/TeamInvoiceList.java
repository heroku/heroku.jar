package com.heroku.api.request.team;

import com.heroku.api.Heroku;
import com.heroku.api.Invoice;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heroku.api.http.HttpUtil.noBody;

/**
 * TODO: Javadoc
 *
 * @author Joe Kutner
 */
public class TeamInvoiceList implements Request<List<Invoice>> {

  private Map<String,String> headers = new HashMap<>();

  private String team;

  public TeamInvoiceList(String teamNameOrId) {
    this.team = teamNameOrId;
  }

  @Override
  public Http.Method getHttpMethod() {
    return Http.Method.GET;
  }

  @Override
  public String getEndpoint() {
    return Heroku.Resource.TeamInvoices.format(team);
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
  public List<Invoice> getResponse(byte[] in, int code, Map<String, String> responseHeaders) {
    if (code == 200) {
      return Json.parse(in, TeamInvoiceList.class);
    } else {
      throw new RequestFailedException("TeamInvoiceList Failed", code, in);
    }
  }
}