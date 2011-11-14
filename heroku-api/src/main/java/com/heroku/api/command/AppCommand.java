package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppCommand implements Command<XmlMapResponse> {

    private final CommandConfig config;

    public AppCommand(String appName) {
        this.config = new CommandConfig().app(appName);
    }

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.App.value, config.get(HerokuRequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw HttpUtil.noBody();
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
    public XmlMapResponse getResponse(InputStream in, int code) {
        if (code == 200)
            return new XmlMapResponse(in);
        else
            throw new RequestFailedException("AppCommand failed", code, in);
    }
}
