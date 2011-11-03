package com.heroku.api.command;

import com.heroku.api.HerokuResource;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingRemoveCommand implements HerokuCommand {

    // delete("/apps/#{app_name}/collaborators/#{escape(email)}").to_s

    private final HerokuCommandConfig config;

    public HerokuSharingRemoveCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + String.format(HerokuResource.Collaborator.value,
                config.get(HerokuRequestKey.name),
                URLEncoder.encode(config.get(HerokuRequestKey.collaborator), "UTF-8"));

        HttpClient client = connection.getHttpClient();
        HttpDelete method = connection.getHttpMethod(new HttpDelete(endpoint));
        method.addHeader(HerokuResponseFormat.XML.acceptHeader);

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

}