package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class HerokuKeysAddCommand implements HerokuCommand {

    // post("/user/keys", key, { 'Content-Type' => 'text/ssh-authkey' }).to_s

    private final String RESOURCE_URL = "/user/keys";
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuKeysAddCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();
        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL;
        PostMethod method = connection.getHttpMethod(new PostMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.JSON.acceptHeader);
        method.addRequestHeader(HerokuContentType.SSH_AUTHKEY.contentType);
        method.setRequestBody(config.get(HerokuRequestKeys.sshkey));
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
