package com.heroku.api;


import com.heroku.api.request.app.AppCreate;
import com.heroku.api.request.app.AppList;
import com.heroku.api.request.key.KeyAdd;
import com.heroku.api.request.key.KeyRemove;
import com.heroku.api.request.response.JsonArrayResponse;
import com.heroku.api.connection.Connection;

public class HerokuAPI {

    Connection<?> connection;

    public static HerokuAPI with(Connection<?> connection) {
        return new HerokuAPI(connection);
    }

    protected HerokuAPI(Connection<?> connection) {
        this.connection = connection;
    }

    public void addKey(String sshKey) {
        connection.execute(new KeyAdd(sshKey));
    }

    public void removeKey(String sshKey) {
        connection.execute(new KeyRemove(sshKey));
    }
    
    public JsonArrayResponse apps() {
        return connection.execute(new AppList());
    }
    
    public HerokuAppAPI app(String name) {
        return new HerokuAppAPI(connection, name);
    }

    public HerokuAppAPI newapp(Heroku.Stack stack) {
        return new HerokuAppAPI(connection, connection.execute(new AppCreate(stack)).name());
    }

    public HerokuAppAPI newapp(Heroku.Stack stack, String appName) {
        return new HerokuAppAPI(connection, connection.execute(new AppCreate(stack).withName(appName)).name());
    }


}
