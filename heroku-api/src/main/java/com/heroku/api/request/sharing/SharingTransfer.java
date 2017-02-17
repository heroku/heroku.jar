package com.heroku.api.request.sharing;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingTransfer implements Request<Unit> {

    private final RequestConfig config;

    public SharingTransfer(String appName, String newOwnerEmail) {
        this.config = new RequestConfig().
            with(Heroku.RequestKey.TransferAppName, appName).
            with(Heroku.RequestKey.TransferOwner, newOwnerEmail);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.POST;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.AppTransfer.value;
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
    public Unit getResponse(byte[] in, int code, Map<String,String> responseHeaders) {
        if (code == Http.Status.CREATED.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("SharingTransfer failed", code, in);
    }
}