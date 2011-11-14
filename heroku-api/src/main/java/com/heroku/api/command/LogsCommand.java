package com.heroku.api.command;


import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.HerokuStack;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogsCommand implements Command<LogsResponse> {

    private CommandConfig config = new CommandConfig().onStack(HerokuStack.Cedar);

    public LogsCommand(String app) {
        config = config.app(app);
    }

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Logs.value, config.get(HerokuRequestKey.name)) + "?logplex=true";
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public Accept getResponseType() {
        return Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public LogsResponse getResponse(InputStream inputStream, int status) {
        if (status == 200) {
            byte[] raw = CommandUtil.getBytes(inputStream);
            URL logs = HttpUtil.toURL(new String(raw));
            return new LogsResponse(new TextCommand(logs), raw);
        } else {
            throw new RequestFailedException("Unable to get logs", status, inputStream);
        }
    }
}
