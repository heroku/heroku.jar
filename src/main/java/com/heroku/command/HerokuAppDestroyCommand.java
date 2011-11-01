package com.heroku.command;

import com.heroku.connection.HerokuAPIException;
import com.heroku.connection.HerokuConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;

import java.io.IOException;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class HerokuAppDestroyCommand implements HerokuCommand {

    private final String RESOURCE_URL = "/apps";
    private final HerokuCommandConfig<HerokuRequestKeys> config;

    public HerokuAppDestroyCommand(HerokuCommandConfig<HerokuRequestKeys> config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws HerokuAPIException, IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString() + RESOURCE_URL +
                "/" + config.get(HerokuRequestKeys.name);
        DeleteMethod method = connection.getHttpMethod(new DeleteMethod(endpoint));
        method.addRequestHeader(HerokuResponseFormat.JSON.acceptHeader);
        client.executeMethod(method);

        return new HerokuCommandMapResponse(method.getResponseBody());
    }

}
