package com.heroku.api.command.log;


import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.TextCommand;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Log implements Command<LogStreamResponse> {

    private CommandConfig config = new CommandConfig().onStack(Heroku.Stack.Cedar);

    public Log(String app) {
        config = config.app(app);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Logs.format(config.get(Heroku.RequestKey.appName)) + "?logplex=true";
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
    public Http.Accept getResponseType() {
        return Http.Accept.TEXT;
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
