package com.heroku.api.command;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.io.InputStream;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class RestartCommand implements Command<EmptyResponse> {

    private final CommandConfig config;
    
    public RestartCommand(String appName) {
        this(new CommandConfig().app(appName));
    }

    RestartCommand(CommandConfig config) {
        this.config = config;
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Restart.value, config.get(HerokuRequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, HerokuRequestKey.processName, HerokuRequestKey.processType);
    }

    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeader.Util.setHeaders(ContentType.FORM_URLENCODED);
    }

    @Override
    public EmptyResponse getResponse(InputStream inputStream, int status) {
        if (status == HttpStatus.OK.statusCode) {
            return new EmptyResponse(inputStream);
        } else {
            throw new RequestFailedException("Unable to restart the process.", status, inputStream);
        }
    }

    public static class NamedProcessRestartCommand extends RestartCommand {
        public NamedProcessRestartCommand(String appName, String processName) {
            super(new CommandConfig().app(appName).with(HerokuRequestKey.processName, processName));
        }
    }

    public static class ProcessTypeRestartCommand extends RestartCommand {
        public ProcessTypeRestartCommand(String appName, String processType) {
            super(new CommandConfig().app(appName).with(HerokuRequestKey.processType, processType));
        }
    }
}
