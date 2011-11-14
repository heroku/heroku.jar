package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingTransferCommand implements Command<EmptyResponse> {

    // heroku.update(app, :transfer_owner => email)
    // put("/apps/#{name}", :app => attributes).to_s
    // http request body = app[transfer_owner]=jw%2Bdemo%40heroku.com

    private final CommandConfig config;

    public SharingTransferCommand(String appName, String newOwnerEmail) {
        this.config = new CommandConfig().app(appName).with(HerokuRequestKey.transferOwner, newOwnerEmail);
    }

    @Override
    public Method getHttpMethod() {
        return Method.PUT;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.App.value, config.get(HerokuRequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, HerokuRequestKey.transferOwner);
    }

    @Override
    public Accept getResponseType() {
        return Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public EmptyResponse getResponse(InputStream in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return new EmptyResponse(in);
        else
            throw new RequestFailedException("SharingTransfer failed", code, in);
    }
}