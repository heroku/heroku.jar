package com.heroku.api.command.ps;

import com.heroku.api.Heroku;
import com.heroku.api.command.Command;
import com.heroku.api.command.CommandConfig;
import com.heroku.api.command.response.Unit;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;

import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class Scale implements Command<Unit> {

    private final CommandConfig config;

    public Scale(String appName, String processType, int quantity) {
        config = new CommandConfig().app(appName).with(Heroku.RequestKey.processType, processType).with(Heroku.RequestKey.quantity, String.valueOf(quantity));
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return String.format(Heroku.Resource.Scale.value, config.get(Heroku.RequestKey.appName));
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public String getBody() {
        return HttpUtil.encodeParameters(config, Heroku.RequestKey.processType, Heroku.RequestKey.quantity);
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.JSON;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.FORM_URLENCODED);
    }

    @Override
    public Unit getResponse(byte[] bytes, int status) {
        if (status == Http.Status.OK.statusCode) {
            return Unit.unit;
        } else if (status == Http.Status.FORBIDDEN.statusCode) {
            throw HttpUtil.insufficientPrivileges(status, bytes);
        } else if (status == Http.Status.UNPROCESSABLE_ENTITY.statusCode) {
            throw new RequestFailedException("Invalid process type", status, bytes);
        } else if (status == Http.Status.PAYMENT_REQUIRED.statusCode) {
            throw new RequestFailedException("Payment is required for scaling this process. " +
                    "Please go to https://api.heroku.com and check your account details.", status, bytes);
        } else {
            throw new RequestFailedException("Error occurred while scaling.", status, bytes);
        }
    }
}
