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
 * @author Naaman Newbold
 */
public class HerokuAppCreateCommand implements HerokuCommand {

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuAppCreateCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL;
        PostMethod method = connection.getHttpMethod(new PostMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.JSON.acceptHeader);
        method.setRequestBody(new NameValuePair[] {
                getParam(HerokuRequestKeys.stack),
                getParam(HerokuRequestKeys.remote),
                getParam(HerokuRequestKeys.timeout),
                getParam(HerokuRequestKeys.addons)
        });
        client.executeMethod(method);

        return new HerokuCommandMapResponse(method.getResponseBody());
    }

    private NameValuePair getParam(HerokuRequestKeys param) {
        return new NameValuePair(param.queryParameter, config.get(param));
    }

}
