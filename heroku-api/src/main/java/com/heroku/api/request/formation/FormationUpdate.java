package com.heroku.api.request.formation;

import com.heroku.api.Formation;
import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.Http.Accept;
import com.heroku.api.http.Http.Method;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;

import java.util.Map;

import static com.heroku.api.parser.Json.parse;

/**
 * @author Román Sosa
 */
public class FormationUpdate implements Request<Formation> {

    private final RequestConfig config;
    private final String processType;
    
    public FormationUpdate(String appName, String processType, int quantity) {

        config = new RequestConfig().app(appName)
                .with(Heroku.RequestKey.Quantity, String.valueOf(quantity));
        this.processType = processType;
    }
    
    @Override
    public Method getHttpMethod() {
        return Http.Method.PATCH;
    }
    
    @Override
    public String getEndpoint() {
        return Heroku.Resource.Formation.format(config.getAppName(), processType);
    }

    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public String getBody() {
        return String.format(config.asJson());
    }
    
    @Override
    public Accept getResponseType() {
        return Accept.JSON;
    }
    
    @Override
    public Map<String, String> getHeaders() {
        return Http.Header.Util.setHeaders(Http.ContentType.JSON);
    }
    
    @Override
    public Formation getResponse(byte[] bytes, int status, Map<String, String> headers) {
        
        if (status == Http.Status.OK.statusCode) {
            return parse(bytes, getClass());
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

    @Override
    public Map<String, ?> getBodyAsMap() {
        return config.asMap();
    }
    
}
