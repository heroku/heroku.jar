package com.heroku.api.command;

import com.heroku.api.HerokuResource;
import com.heroku.api.connection.HerokuAPIException;
import com.heroku.api.connection.HerokuConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingTransferCommand implements HerokuCommand {

    // heroku.update(app, :transfer_owner => email)
    // put("/apps/#{name}", :app => attributes).to_s
    // http request body = app[transfer_owner]=jw%2Bdemo%40heroku.com

    private final HerokuCommandConfig config;

    public HerokuSharingTransferCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + String.format(HerokuResource.App.value, config.get(HerokuRequestKey.name));

        HttpClient client = connection.getHttpClient();
        HttpPut method = connection.getHttpMethod(new HttpPut(endpoint));
        method.addHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                getParam(HerokuRequestKey.transferOwner)
        )));

        HttpResponse response = client.execute(method);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandEmptyResponse(success);
    }

    private BasicNameValuePair getParam(HerokuRequestKey param) {
        return new BasicNameValuePair(param.queryParameter, config.get(param));
    }

}