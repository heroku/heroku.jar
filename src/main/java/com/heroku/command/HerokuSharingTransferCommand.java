package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingTransferCommand implements HerokuCommand {

    // heroku.update(app, :transfer_owner => email)
    // put("/apps/#{name}", :app => attributes).to_s
    // http request body = app[transfer_owner]=jw%2Bdemo%40heroku.com

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig config;

    public HerokuSharingTransferCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKey.name);

        HttpEntity requestEntity = new StringEntity("app[transfer_owner]=" +
                URLEncoder.encode(config.get(HerokuRequestKey.collaborator), "UTF-8"));

        HttpClient client = connection.getHttpClient();
        HttpPut method = connection.getHttpMethod(new HttpPut(endpoint));
        method.addHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setEntity(requestEntity);

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

}