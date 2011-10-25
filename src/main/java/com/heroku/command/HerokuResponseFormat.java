package com.heroku.command;

import org.apache.commons.httpclient.Header;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HerokuResponseFormat {
    JSON ("json", "application/json"),
    XML ("xml", "text/xml");

    public final String value;
    public final Header acceptHeader;

    HerokuResponseFormat(String value, String mimeType) {
        this.value = value;
        this.acceptHeader = new Header("Accept", mimeType);
    }
}
