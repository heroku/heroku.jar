package com.heroku.api.exception;

/**
 * Generic {@link RuntimeException} thrown when using heroku.jar.
 *
 * @author Naaman Newbold
 */
public class HerokuAPIException extends RuntimeException {


    public HerokuAPIException(String msg) {
        super(msg);
    }

    public HerokuAPIException(Throwable throwable) {
        super(throwable);
    }

    public HerokuAPIException(String msg, Throwable throwable) {
        super(msg, throwable);
    }


}
