package com.heroku.api.command;

import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuConfigAddCommand implements HerokuCommand {

    // put("/apps/#{app_name}/config_vars", json_encode(new_vars)).to_s

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig config;

    public HerokuConfigAddCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKey.name) + "/config_vars";

        HttpEntity requestEntity = new StringEntity(config.get(HerokuRequestKey.configvars));

        HttpPut method = connection.getHttpMethod(new HttpPut(endpoint));
        method.addHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setEntity(requestEntity);

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
        
        return new HerokuCommandEmptyResponse(success);
    }

}