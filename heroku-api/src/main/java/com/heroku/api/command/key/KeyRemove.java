package com.heroku.api.command.key;

import com.heroku.api.HerokuRequestKey;
import com.heroku.api.HerokuResource;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.Unit;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Accept;
import com.heroku.api.http.HttpStatus;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.http.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class KeyRemove implements Command<Unit> {

    // delete("/user/keys/#{escape(key)}").to_s

    private final CommandConfig config;

    public KeyRemove(String keyName) {
        this.config = new CommandConfig().with(HerokuRequestKey.appName, keyName);
    }

    @Override
    public Method getHttpMethod() {
        return Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Key.value, config.get(HerokuRequestKey.appName));
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
    public Accept getResponseType() {
        return Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    @Override
    public Unit getResponse(byte[] in, int code) {
        if (code == HttpStatus.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("KeysRemove failed", code, in);
    }
}