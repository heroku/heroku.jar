package com.heroku.api.exception;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class LoginFailedException extends RequestFailedException {
    public LoginFailedException(String msg, int code, byte[] in) {
        super(msg, code, in);
    }

    public LoginFailedException(String msg, int code, String body) {
        super(msg, code, body);
    }
}
