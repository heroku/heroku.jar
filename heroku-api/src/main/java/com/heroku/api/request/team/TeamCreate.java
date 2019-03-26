package com.heroku.api.request.team;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.Team;
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
public class TeamCreate implements Request<Team> {

    private final RequestConfig config;

    public TeamCreate(Team team) {
        RequestConfig builder = new RequestConfig();
        builder = (team.getName() != null) ? builder.with(Heroku.RequestKey.TeamName, team.getName()) : builder;
        config = builder;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Teams.value;
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
    public Team getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.OK.statusCode)
            return parse(in, getClass());
        else if (code == Http.Status.CREATED.statusCode)
            return parse(in, getClass());
        else if (code == Http.Status.ACCEPTED.statusCode)
            return parse(in, getClass());
        else
            throw new RequestFailedException("Failed to create team", code, in);
    }
}
