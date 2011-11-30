package com.heroku.api.exception;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class ResponseFailedException extends HerokuAPIException {
    public ResponseFailedException(String msg) {
        super(msg);
    }

    public ResponseFailedException(Throwable throwable) {
        super(throwable);
    }

    public ResponseFailedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
