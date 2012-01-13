package com.heroku.api.request.log;


import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.heroku.api.Heroku.RequestKey.*;

public class Log implements Request<LogStreamResponse> {

    private final RequestConfig config;

    public Log(String app) {
        this(logFor(app).getConfig());
    }
    
    public Log(String app, boolean tail) {
        this(logFor(app).tail(tail).getConfig());
    }
    
    Log(RequestConfig config) {
        this.config = config;
    }
    
    public Log(LogRequestBuilder logRequest) {
		this(logRequest.getConfig());
	}

	public static LogRequestBuilder logFor(String app) {
        return new LogRequestBuilder().app(app);
    }
    
    public static class LogRequestBuilder {
        RequestConfig config = new RequestConfig().with(Logplex, "true");
        
        public Log getRequest() {
            return new Log(config);
        }
        
        RequestConfig getConfig() {
            return config;
        }

        LogRequestBuilder add(Heroku.RequestKey key, String val) {
            config = config.with(key, val);
            return this;
        }
        
        public LogRequestBuilder app(String app) {
            return add(AppName, app);
        }

        public LogRequestBuilder num(int num) {
            return add(LogNum, String.valueOf(num));
        }

        public LogRequestBuilder ps(String processName) {
            return add(ProcessName, processName);
        }

        public LogRequestBuilder source(String source) {
            return add(LogSource, source);
        }

        public LogRequestBuilder tail(boolean tail) {
            return (tail) ? add(LogTail, "1") : this;
        }
        
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.GET;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Logs.format(
                config.get(AppName),
                HttpUtil.encodeParameters(config, Logplex, LogNum, ProcessName, LogSource, LogTail)
        );
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
