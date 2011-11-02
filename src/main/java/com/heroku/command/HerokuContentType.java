package com.heroku.command;

import org.apache.http.message.BasicHeader;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public enum HerokuContentType {
    SSH_AUTHKEY ("text/ssh-authkey");

    public final BasicHeader contentType;

    HerokuContentType(String mimeType) {
        this.contentType = new BasicHeader("Content-Type", mimeType);
    }
}