package com.heroku.api.http;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HttpStatus {
    OK(200), ACCEPTED(202), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404),  UNPROCESSABLE_ENTITY(422);

    public final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }
}
