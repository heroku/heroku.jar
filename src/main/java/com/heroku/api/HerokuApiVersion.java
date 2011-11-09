package com.heroku.api;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HerokuApiVersion {
    v2 (2),
    v3 (3);

    public static final String HEADER = "X-Heroku-API-Version";

    public final int version;

    HerokuApiVersion(int version) {
        this.version = version;
    }
}
