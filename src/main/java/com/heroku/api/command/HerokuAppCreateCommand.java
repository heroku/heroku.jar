package com.heroku.api.command;

import com.heroku.api.HerokuResource;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuAppCreateCommand implements HerokuCommand {

    private final String RESOURCE_URL = HerokuResource.Apps.value;
    private final HerokuCommandConfig config;

    public HerokuAppCreateCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL;

        HttpPost method = connection.getHttpMethod(new HttpPost(endpoint));
        method.addHeader(HerokuResponseFormat.JSON.acceptHeader);

        method.setEntity(new UrlEncodedFormEntity(Arrays.asList(
            getParam(HerokuRequestKey.stack),
            getParam(HerokuRequestKey.remote),
            getParam(HerokuRequestKey.timeout),
            getParam(HerokuRequestKey.addons)
        )));
        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED);

        return new HerokuCommandMapResponse(EntityUtils.toByteArray(response.getEntity()), success);
    }

    private BasicNameValuePair getParam(HerokuRequestKey param) {
        return new BasicNameValuePair(param.queryParameter, config.get(param));
    }

}
