package com.heroku.api.http;

import com.heroku.api.Heroku;

import java.util.HashMap;
import java.util.Map;


public class Http {
    public static enum Accept implements Header {
        JSON("application/json"),
        XML("text/xml"),
        TEXT("text/plain");

        private String value;
        static String ACCEPT = "Accept";

        Accept(String val) {
            this.value = val;
        }

        @Override
        public String getHeaderName() {
            return ACCEPT;
        }

        @Override
        public String getHeaderValue() {
            return value;
        }

    }

    public static enum ContentType implements Header {
        FORM_URLENCODED("application/x-www-form-urlencoded"),
        SSH_AUTHKEY("text/ssh-authkey");

        private String value;
        static String CONTENT_TYPE = "Content-Type";

        ContentType(String val) {
            this.value = val;
        }

        @Override
        public String getHeaderName() {
            return CONTENT_TYPE;
        }

        @Override
        public String getHeaderValue() {
            return value;
        }

    }
    
    public static enum UserAgent implements Header {
        LATEST("heroku.jar-%s-v%s");

        static final String USER_AGENT = "User-Agent";
        private final String userAgent;

        UserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        @Override
        public String getHeaderName() {
            return USER_AGENT;
        }

        @Override
        public String getHeaderValue() {
          return getHeaderValue("unspecified");
        }

        public String getHeaderValue(String customPart) {
            return String.format(userAgent, customPart, Heroku.JarProperties.getProperty("heroku.jar.version"));
        }
    }

    public static interface Header {

        public static class Util {
            public static Map<String, String> setHeaders(Header... headers) {
                Map<String, String> headerMap = new HashMap<String, String>();
                for (Header h : headers) {
                    headerMap.put(h.getHeaderName(), h.getHeaderValue());
                }
                return headerMap;
            }
        }

        String getHeaderName();

        String getHeaderValue();

    }

    public static enum Method {GET, PUT, POST, DELETE}


    public static enum Status {
        OK(200), CREATED(201), ACCEPTED(202), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404), UNPROCESSABLE_ENTITY(422);

        public final int statusCode;

        Status(int statusCode) {
            this.statusCode = statusCode;
        }
    }
}
