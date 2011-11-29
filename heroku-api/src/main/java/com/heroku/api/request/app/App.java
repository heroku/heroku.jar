package com.heroku.api.request.app;

import com.heroku.api.Heroku;
import com.heroku.api.request.Response;
import static com.heroku.api.Heroku.ResponseKey.*;
import static com.heroku.api.request.response.FormatUtil.*;

import java.util.Date;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class App {
    private final Response data;

    public App(Response res) {
        this.data = res;
    }

    public String id() {
        return asString(data.get(Id));
    }
    public String name() {
        return asString(data.get(Name));
    }
    public String create_status() {
        return asString(data.get(CreateStatus));
    }
    public Date created_at() {
        return asDate(data.get(CreatedAt));
    }
    public Heroku.Stack stack() {
        return asStack(data.get(Stack));
    }
    public String repo_migrate_status() {
        return asString(data.get(RepoMigrateStatus));
    }
    public int slug_size() {
        return asInt(data.get(SlugSize));
    }
    public int repo_size() {
        return asInt(data.get(RepoSize));
    }
    public int dynos() {
        return asInt(data.get(Dynos));
    }
    public int workers() {
        return asInt(data.get(Workers));
    }
    
}
