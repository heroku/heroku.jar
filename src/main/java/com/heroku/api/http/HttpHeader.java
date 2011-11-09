package com.heroku.api.http;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public final class HttpHeader {

    public static final class ContentType {
        public final static String HEADER = "Content-Type";
        public final static String FORM_URLENCODED = "application/x-www-form-urlencoded";
        public final static String SSH_AUTHKEY = "text/ssh-authkey";
    }
    
    public static final class Accept {
        public final static String HEADER = "Accept";
        public final static String APPLICATION_JSON = "application/json";
        public final static String TEXT_XML = "text/xml";
    }

}
