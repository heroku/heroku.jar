package com.heroku.api.request.team;

import com.heroku.api.Heroku;
import com.heroku.api.TeamApp;
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
 * TODO: Javadoc
 *
 * @author Joe Kutner
 */
public class TeamAppList implements Request<Range<TeamApp>> {

    private Map<String,String> headers = new HashMap<>();

    private String team;

    public TeamAppList(String teamNameOrId) {
        this.team = teamNameOrId;
    }

    public TeamAppList(String teamNameOrId, String range) {
        this(teamNameOrId);
        this.headers.put("Range", range);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.TeamApps.format(team);
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
    public Range<TeamApp> getResponse(byte[] in, int code, Map<String, String> responseHeaders) {
        if (code == 200) {
            return Json.parse(in, TeamAppList.class);
        } else if (code == 206) {
            Range<TeamApp> r = Json.parse(in, TeamAppList.class);
            r.setNextRange(responseHeaders.get("Next-Range"));
            return r;
        } else {
            throw new RequestFailedException("TeamAppList Failed", code, in);
        }
    }
}