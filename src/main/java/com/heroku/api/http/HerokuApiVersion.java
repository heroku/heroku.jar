package com.heroku.api.http;

import com.heroku.api.http.HttpHeader;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public enum HerokuApiVersion implements HttpHeader {

    v2(2),
    v3(3);

    public static final String HEADER = "X-Heroku-API-Version";

    public final int version;

    HerokuApiVersion(int version) {
        this.version = version;
    }


    @Override
    public String getHeaderName() {
        return HEADER;
    }

    @Override
    public String getHeaderValue() {
        return Integer.toString(version);
    }
}
