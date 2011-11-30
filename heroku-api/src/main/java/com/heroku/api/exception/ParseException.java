package com.heroku.api.exception;

/**
 * TODO: Javadoc
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
