package com.heroku.connection;

import com.heroku.command.HerokuCommand;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;
import java.net.URL;

/**
 * Connection manager for connecting to the Heroku API.
 *
 * @author Naaman Newbold
 */
public interface HerokuConnection {
    void executeCommand(HerokuCommand command) throws HerokuAPIException, IOException;
    HttpClient getHttpClient();
    <T extends HttpMethod> T getHttpMethod(T method);
    URL getEndpoint();
    String getEmail();
    String getApiKey();
}
