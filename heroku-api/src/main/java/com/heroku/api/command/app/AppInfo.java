package com.heroku.api.command.app;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.XmlMapResponse;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class AppInfo implements Command<XmlMapResponse> {

    private final CommandConfig config;

    public AppInfo(String appName) {
        this.config = new CommandConfig().app(appName);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.App.format(config.get(Heroku.RequestKey.appName));
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
    public Http.Accept getResponseType() {
        return Http.Accept.XML;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public XmlMapResponse getResponse(byte[] in, int code) {
        if (code == 200)
            return new XmlMapResponse(in);
        else
            throw new RequestFailedException("Unable to get app info", code, in);
    }
}
