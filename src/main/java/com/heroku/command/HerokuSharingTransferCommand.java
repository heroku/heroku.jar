package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

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
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuSharingTransferCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKeys.app);

        RequestEntity requestEntity = new StringRequestEntity("app[transfer_owner]=" + URLEncoder.encode(config.get(HerokuRequestKeys.collaborator), "UTF-8"), "application/x-www-form-urlencoded", "UTF-8");

        HttpClient client = connection.getHttpClient();
        PutMethod method = connection.getHttpMethod(new PutMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setRequestEntity(requestEntity);
        client.executeMethod(method);

        if (method.getStatusCode() != 200)
        {
            throw new HerokuAPIException(method.getStatusText());
        }

        // successful response is an empty string
        return new HerokuCommandMapResponse();
    }

    private NameValuePair getParam(HerokuRequestKeys param) {
        return new NameValuePair(param.queryParameter, config.get(param));
    }

}
