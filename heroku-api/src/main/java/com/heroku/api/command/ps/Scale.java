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
public class Scale implements Command<Unit> {

    private final CommandConfig config;

    public Scale(String appName, String processType, int quantity) {
        config = new CommandConfig().app(appName).with(HerokuRequestKey.processType, processType).with(HerokuRequestKey.quantity, String.valueOf(quantity));
    }

    @Override
    public Method getHttpMethod() {
        return Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(HerokuResource.Scale.value, config.get(HerokuRequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, HerokuRequestKey.processType, HerokuRequestKey.quantity);
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
        } else if (status == HttpStatus.FORBIDDEN.statusCode) {
            throw HttpUtil.insufficientPrivileges(status, bytes);
        } else if (status == HttpStatus.UNPROCESSABLE_ENTITY.statusCode) {
            throw new RequestFailedException("Invalid process type", status, bytes);
        } else if (status == HttpStatus.PAYMENT_REQUIRED.statusCode) {
            throw new RequestFailedException("Payment is required for scaling this process. " +
                    "Please go to https://api.heroku.com and check your account details.", status, bytes);
        } else {
            throw new RequestFailedException("Error occurred while scaling.", status, bytes);
        }
    }
}
