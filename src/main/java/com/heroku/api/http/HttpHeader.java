package com.heroku.api.http;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public interface HttpHeader {

    static String CONTENT_TYPE = "Content-Type";
    static String ACCEPT = "Accept";


    public static class Util {
        public static Map<String, String> setHeaders(HttpHeader... headers) {
            Map<String, String> headerMap = new HashMap<String, String>();
            for (HttpHeader h : headers) {
                headerMap.put(h.getHeaderName(), h.getHeaderValue());
            }
            return headerMap;
        }
    }


    String getHeaderName();

    String getHeaderValue();



}
