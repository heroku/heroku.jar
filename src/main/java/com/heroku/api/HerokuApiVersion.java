package com.heroku.api;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HerokuApiVersion {
    v3 (3),
    v2 (2);

    public final int version;
    public final Header versionHeader;

    HerokuApiVersion(int version) {
        this.version = version;
        this.versionHeader = new BasicHeader("X-Heroku-API-Version", String.valueOf(version));
    }
}
