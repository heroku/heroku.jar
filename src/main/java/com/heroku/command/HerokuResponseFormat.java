package com.heroku.command;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

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
        this.acceptHeader = new BasicHeader("Accept", mimeType);
    }
}
