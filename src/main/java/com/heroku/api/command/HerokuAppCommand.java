package com.heroku.api.command;

import com.heroku.api.HerokuResource;
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
public class HerokuAppCommand implements HerokuCommand {

    private final HerokuCommandConfig config;

    public HerokuAppCommand(HerokuCommandConfig config) {
        this.config = config;
    }

    @Override
    public HerokuCommandResponse execute(HerokuConnection connection) throws IOException {
        HttpClient client = connection.getHttpClient();

        String endpoint = connection.getEndpoint().toString()
                 + String.format(HerokuResource.App.value, config.get(HerokuRequestKey.name));

        HttpGet get = connection.getHttpMethod(new HttpGet(endpoint));
        get.addHeader(HerokuResponseFormat.XML.acceptHeader);

        HttpResponse response = client.execute(get);
        boolean success = (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        return new HerokuCommandXmlMapResponse(EntityUtils.toByteArray(response.getEntity()), success);
    }
}
