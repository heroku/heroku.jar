package com.heroku.api.exception;

/**
 * {@link RuntimeException} that's thrown when response parsing fails to parse data returned by the API.
 *
 * @author Naaman Newbold
 */
public class ParseException extends HerokuAPIException {
    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable throwable) {
        super(throwable);
    }

    public ParseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
