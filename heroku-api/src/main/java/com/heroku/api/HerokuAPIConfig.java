package com.heroku.api;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuAPIConfig {

    private final String username;
    private final String password;
    private final String apiKey;

    public HerokuAPIConfig() {
        this.username = null;
        this.password = null;
        this.apiKey = null;
    }

    @Override
    public String toString() {
        return String.format(
                "{username:'%s',password:'%s',apiKey:'%s'}",
                username, password, apiKey
        );
    }

    private HerokuAPIConfig(String username, String password, String apiKey) {
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
    }

    public HerokuAPIConfig setUsername(String username) {
        return new HerokuAPIConfig(username, this.password, this.apiKey);
    }

    public HerokuAPIConfig setPassword(String password) {
        return new HerokuAPIConfig(this.username, password, this.apiKey);
    }

    public HerokuAPIConfig setApiKey(String apiKey) {
        return new HerokuAPIConfig(this.username, this.password, apiKey);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getApiKey() {
        return apiKey;
    }
}
