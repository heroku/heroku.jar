package com.heroku.api.command.log;


import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.HerokuStack;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.CommandUtil;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogStream implements Command<LogStreamResponse> {

    private CommandConfig config = new CommandConfig().onStack(HerokuStack.Cedar);

    public LogStream(String app) {
        config = config.app(app);
    }

    @Override
    public Method getHttpMethod() {
        return Method.GET;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Logs.value, config.get(HerokuRequestKey.appName)) + "?logplex=true&tail=1";
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
    public LogStreamResponse getResponse(byte[] bytes, int status) {
        if (status == 200) {
            URL logs = HttpUtil.toURL(new String(bytes));
            return new LogStreamResponse(logs);
        } else {
            throw new RequestFailedException("Unable to get logs", status, bytes);
        }
    }
}
