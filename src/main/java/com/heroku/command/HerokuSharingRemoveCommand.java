package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuSharingRemoveCommand implements HerokuCommand {

    // delete("/apps/#{app_name}/collaborators/#{escape(email)}").to_s

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuSharingRemoveCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL + "/" + config.get(HerokuRequestKeys.app) + "/collaborators/" + URLEncoder.encode(config.get(HerokuRequestKeys.collaborator), "UTF-8");

        HttpClient client = connection.getHttpClient();
        DeleteMethod method = connection.getHttpMethod(new DeleteMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.XML.acceptHeader);
        client.executeMethod(method);

        if (method.getStatusCode() != 200)
        {
            throw new HerokuAPIException(method.getStatusText());
        }

        // response comes back as a string like: jw+demo@heroku.com has been removed as collaborator on empty-fire-6651
        return new HerokuCommandMapResponse();
    }

    private NameValuePair getParam(HerokuRequestKeys param) {
        return new NameValuePair(param.queryParameter, config.get(param));
    }

}
