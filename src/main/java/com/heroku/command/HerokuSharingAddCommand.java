package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingAddCommand implements HerokuCommand {

    // xml(post("/apps/#{app_name}/collaborators", { 'collaborator[email]' => email }).to_s)

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuSharingAddCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKeys.app) + "/collaborators";
        PostMethod method = connection.getHttpMethod(new PostMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.XML.acceptHeader);
        method.setRequestBody(new NameValuePair[] {getParam(HerokuRequestKeys.collaborator)});
        client.executeMethod(method);

        if (method.getStatusCode() != 200)
        {
            throw new HerokuAPIException(method.getStatusText());
        }

        // successful response is a string like: demo@heroku.com added as a collaborator on fierce-meadow-5651
        return new HerokuCommandMapResponse();
    }

    private NameValuePair getParam(HerokuRequestKeys param) {
        return new NameValuePair(param.queryParameter, config.get(param));
    }

}
