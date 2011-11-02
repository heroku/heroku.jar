package com.heroku.api.command;

import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuKeysAddCommand implements HerokuCommand {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

    private final String RESOURCE_URL = "/user/keys";
    private final HerokuCommandConfig config;

    public HerokuKeysAddCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL;
        HttpPost method = connection.getHttpMethod(new HttpPost(endpoint));
        method.addHeader(HerokuResponseFormat.JSON.acceptHeader);
        method.addHeader(HerokuContentType.SSH_AUTHKEY.contentType);
        method.setEntity(new StringEntity(config.get(HerokuRequestKey.sshkey)));

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

}