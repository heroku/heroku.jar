package com.heroku.api.command.ps;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.Unit;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.*;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Restart implements Command<Unit> {

    private final CommandConfig config;

    public Restart(String appName) {
        this(new CommandConfig().app(appName));
    }

    Restart(CommandConfig config) {
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
    public Unit getResponse(byte[] bytes, int status) {
        if (status == HttpStatus.OK.statusCode) {
            return Unit.unit;
        } else {
            throw new RequestFailedException("Unable to restart the process.", status, bytes);
        }
    }

    public static class NamedProcessRestart extends Restart {
        public NamedProcessRestart(String appName, String processName) {
            super(new CommandConfig().app(appName).with(HerokuRequestKey.processName, processName));
        }
    }

    public static class ProcessTypeRestart extends Restart {
        public ProcessTypeRestart(String appName, String processType) {
            super(new CommandConfig().app(appName).with(HerokuRequestKey.processType, processType));
        }
    }
}
