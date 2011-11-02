package com.heroku.api.command;

import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;


import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuKeysRemoveCommand implements HerokuCommand {

    // delete("/user/keys/#{escape(key)}").to_s

    private final String RESOURCE_URL = "/user/keys";
    private final HerokuCommandConfig config;

    public HerokuKeysRemoveCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL +
                        "/" + config.get(HerokuRequestKey.name);
        HttpDelete method = connection.getHttpMethod(new HttpDelete(endpoint));
        method.addHeader(HerokuResponseFormat.JSON.acceptHeader);

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

}