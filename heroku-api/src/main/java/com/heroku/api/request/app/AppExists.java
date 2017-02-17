package com.heroku.api.request.app;

import com.heroku.api.App;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.request.RequestTransformation;

import java.util.Map;

/**
 * Determines if an app with a given name exists on Heroku.
 * A true response does not necessarily indicate the user has access to the app.
 *
 * @author Ryan Brainard
 */
public class AppExists extends RequestTransformation<App,Boolean> {

    public AppExists(String name) {
        super(new AppInfo(name));
    }

    @Override
    public Boolean getResponse(byte[] data, int code, Map<String,String> responseHeaders) {
        if (Http.Status.OK.equals(code) || Http.Status.FORBIDDEN.equals(code)) {
            return true;
        } else if (Http.Status.NOT_FOUND.equals(code)) {
            return false;
        } else {
            throw new RequestFailedException("Unexpected app exist status", code, data);
        }
    }
}