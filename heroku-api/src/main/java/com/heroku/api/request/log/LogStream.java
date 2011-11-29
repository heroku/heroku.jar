package com.heroku.api.request.log;


import com.heroku.api.Heroku;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogStream implements Request<LogStreamResponse> {

    private RequestConfig config = new RequestConfig().onStack(Heroku.Stack.Cedar);

    public LogStream(String app) {
        config = config.app(app);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Logs.format(config.get(Heroku.RequestKey.AppName)) + "?logplex=true&tail=1";
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
