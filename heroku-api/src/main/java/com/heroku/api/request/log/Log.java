package com.heroku.api.request.log;


import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import static com.heroku.api.Heroku.RequestKey.*;
import static com.heroku.api.http.HttpUtil.noBody;

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

    /**
     * Builder class for specifying log request parameters. Logs can be retrieved by specifying a process,
     * the number of lines, etc... Once the params are specified, build the {@link Log} request with {@link #getRequest()}.
     *
     * Use this builder to specify which params a {@link Log} should use. e.g.
     * <pre>{@code new Log.LogRequestBuilder().app("myApp").ps("web.1").num(50).getRequest()}</pre>
     */
    public static class LogRequestBuilder {
        RequestConfig config = new RequestConfig().with(Logplex, "true");

        /**
         * Finalize the params and get a Log request.
         * @return The log
         */
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

        /**
         * Name of the app to get logs for.
         * @param app App name.
         * @return builder object
         */
        public LogRequestBuilder app(String app) {
            return add(AppName, app);
        }

        /**
         * Number of log lines to retrieve.
         * @param num
         * @return builder object
         */
        public LogRequestBuilder num(int num) {
            return add(LogNum, String.valueOf(num));
        }

        /**
         * Name of the process to get logs for. Process names can be seen by running {@link com.heroku.api.HerokuAPI#listProcesses}, or by
         * retrieving all logs and inspecting the value inside the brackets (e.g. web.1 from app[web.1]).
         * @param processName Name of the process. e.g. "web.1"
         * @return builder object
         */
        public LogRequestBuilder ps(String processName) {
            return add(ProcessName, processName);
        }

        /**
         * The source of the logs. This can be found by reviewing the logs and looking at the value just after the timestamp. e.g. the source of
         * <code>2012-03-23T18:46:01+00:00 heroku[router]</code> is "heroku".
         * @param source
         * @return builder object
         */
        public LogRequestBuilder source(String source) {
            return add(LogSource, source);
        }

        /**
         * Whether or not to tail the logs. If true, a stream will be created that will remain open for an extended period of time.
         * @param tail
         * @return builder object
         */
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
    public Map<String,Object> getBodyAsMap() {
        return null;
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public LogStreamResponse getResponse(byte[] bytes, int status) {
        if (status == 200) {
            try {
                URL logs = HttpUtil.toURL(new String(bytes));
                return new LogStreamResponse(logs);
            } catch (RuntimeException e) {
                throw new RequestFailedException(e.getMessage(), status, bytes);
            }
        } else {
            throw new RequestFailedException("Unable to get logs", status, bytes);
        }
    }

}
