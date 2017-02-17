package com.heroku.api.request.log;


import com.heroku.api.Heroku;
import com.heroku.api.LogSession;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.parser.Json;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.net.URL;
import java.util.Collections;
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

    /**
     * Builder class for specifying log request parameters. Logs can be retrieved by specifying a process,
     * the number of lines, etc... Once the params are specified, build the {@link Log} request with {@link #getRequest()}.
     *
     * Use this builder to specify which params a {@link Log} should use. e.g.
     * <pre>{@code new Log.LogRequestBuilder().app("myApp").ps("web.1").num(50).getRequest()}</pre>
     */
    public static class LogRequestBuilder {
        RequestConfig config = new RequestConfig();

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
            config = config.app(app);
            return this;
        }

        /**
         * Number of log lines to retrieve.
         * @param num number of lines
         * @return builder object
         */
        public LogRequestBuilder num(int num) {
            return add(LogLines, String.valueOf(num));
        }

        /**
         * Name of the dyno to get logs for (e.g. web.1).
         * @param dyno Name of the dyno. e.g. "web.1"
         * @return builder object
         */
        public LogRequestBuilder dyno(String dyno) {
            return add(Dyno, dyno);
        }

        /**
         * The source of the logs. This can be found by reviewing the logs and looking at the value just after the timestamp. e.g. the source of
         * <code>2012-03-23T18:46:01+00:00 heroku[router]</code> is "heroku".
         * @param source source of the logs
         * @return builder object
         */
        public LogRequestBuilder source(String source) {
            return add(LogSource, source);
        }

        /**
         * Whether or not to tail the logs. If true, a stream will be created that will remain open for an extended period of time.
         * @param tail to tail the logs or not
         * @return builder object
         */
        public LogRequestBuilder tail(boolean tail) {
            return (tail) ? add(LogTail, "true") : this;
        }
        
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Logs.format(config.getAppName());
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return config.asJson();
    }

    @Override
    public Map<String,Object> getBodyAsMap() {
        return config.asMap();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public LogStreamResponse getResponse(byte[] bytes, int status, Map<String,String> responseHeaders) {
        if (Http.Status.CREATED.equals(status)) {
            try {
                LogSession logSession = Json.parse(bytes, LogSession.class);
                URL logs = HttpUtil.toURL(logSession.getLogplex_url());
                return new LogStreamResponse(logs);
            } catch (RuntimeException e) {
                throw new RequestFailedException(e.getMessage(), status, bytes);
            }
        } else {
            throw new RequestFailedException("Unable to get logs", status, bytes);
        }
    }

}
