package com.heroku.api.http;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HttpStatus {
    OK(200), ACCEPTED(202), FORBIDDEN(403), NOT_FOUND(404);

    public final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
