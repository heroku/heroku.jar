package com.heroku.api.command;

import com.heroku.api.HerokuResource;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuAppsCommand implements HerokuCommand {

    private final String RESOURCE_URL = HerokuResource.Apps.value;
    private final HerokuCommandConfig config;

    public HerokuAppsCommand(HerokuCommandConfig config) {
        this.config = config;
    }


    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL;

        HttpClient client = connection.getHttpClient();
        HttpGet getMethod = connection.getHttpMethod(new HttpGet(endpoint));
        getMethod.addHeader(HerokuResponseFormat.JSON.acceptHeader);
        HttpResponse response = client.execute(getMethod);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandListMapResponse(EntityUtils.toByteArray(response.getEntity()), success);
    }
}
