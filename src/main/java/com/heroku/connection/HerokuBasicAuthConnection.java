package com.heroku.connection;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuBasicAuthConnection implements HerokuConnection {

    private final String endpoint;
    private final String apiKey;
    private final String userId;
    private final String userEmail;

    public HerokuBasicAuthConnection(String endpoint, String apiKey, String userId, String userEmail) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    @Override
    public String getApiKey() {
        return apiKey;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return userEmail;
    }
}
