package com.heroku.api.request.team;

import com.heroku.api.App;
import com.heroku.api.Heroku;
import com.heroku.api.TeamApp;
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
 * @author Naaman Newbold
 */
public class TeamAppCreate implements Request<TeamApp> {

    private final RequestConfig config;

    public TeamAppCreate(TeamApp app) {
        RequestConfig builder = new RequestConfig();
        builder = builder.with(Heroku.RequestKey.Team, app.getTeam().getName());
        builder = (app.getName() != null) ? builder.with(Heroku.RequestKey.AppName, app.getName()) : builder;
        builder = (app.getStack() != null) ? builder.with(Heroku.RequestKey.Stack, app.getStack().getName()) : builder;
        config = builder;
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.TeamAppsAll.value;
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
    public TeamApp getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.CREATED.statusCode)
            return parse(in, getClass());
        else if (code == Http.Status.ACCEPTED.statusCode)
            return parse(in, getClass());
        else
            throw new RequestFailedException("Failed to create team app", code, in);
    }
}
