package com.heroku.connection;

import java.io.IOException;

/**
 * TODO: Enter JavaDoc
 *
 * @author Naaman Newbold
 */
public class HerokuAPIException extends Exception {
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
