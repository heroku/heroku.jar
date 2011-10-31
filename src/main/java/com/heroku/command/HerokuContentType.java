package com.heroku.command;

import org.apache.commons.httpclient.Header;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public enum HerokuContentType {
    SSH_AUTHKEY ("text/ssh-authkey");

    public final Header contentType;

    HerokuContentType(String mimeType) {
        this.contentType = new Header("Content-Type", mimeType);
    }
}