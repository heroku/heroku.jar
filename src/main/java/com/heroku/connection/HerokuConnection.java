package com.heroku.connection;

import com.heroku.command.HerokuCommand;
import org.apache.http.HttpMessage;
import org.apache.http.client.HttpClient;

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
    <T extends HttpMessage> T getHttpMethod(T method);
    URL getEndpoint();
    String getEmail();
    String getApiKey();
}
