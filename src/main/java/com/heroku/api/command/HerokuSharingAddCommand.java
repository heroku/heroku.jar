package com.heroku.api.command;

import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingAddCommand implements HerokuCommand {

    // xml(post("/apps/#{app_name}/collaborators", { 'collaborator[email]' => email }).to_s)

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig config;

    public HerokuSharingAddCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKey.name) + "/collaborators";
        HttpPost method = connection.getHttpMethod(new HttpPost(endpoint));
        method.addHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setEntity(new UrlEncodedFormEntity(Arrays.asList(
            getParam(HerokuRequestKey.collaborator)
        )));

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

    private BasicNameValuePair getParam(HerokuRequestKey param) {
        return new BasicNameValuePair(param.queryParameter, config.get(param));
    }

}
