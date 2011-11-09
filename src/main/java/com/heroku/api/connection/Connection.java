package com.heroku.api.connection;

import com.heroku.api.command.Command;
import com.heroku.api.command.CommandResponse;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.net.URL;

/**
 * Connection manager for connecting to the Heroku API.
 *
 * @author Naaman Newbold
 */
public interface Connection {

    <T extends CommandResponse> T executeCommand(Command<T> command) throws IOException;

    HttpClient getHttpClient();

    URL getEndpoint();

    String getEmail();

    String getApiKey();

}
