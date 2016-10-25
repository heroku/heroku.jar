package com.heroku.api.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Simple abstraction for HTTP request/response values.
 */
public class Http {
    /**
     * HTTP Accept header model.
     */
    public static enum Accept implements Header {
        JSON("application/json"),
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

    /**
     * HTTP Content-Type header model.
     */
    public static enum ContentType implements Header {
        JSON("application/json"),
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

    /**
     * HTTP User-Agent header model.
     *
     * @see UserAgentValueProvider
     */
    public static enum UserAgent implements Header {
        LATEST(loadValueProvider());

        static final String USER_AGENT = "User-Agent";
        private final UserAgentValueProvider userAgentValueProvider;

        UserAgent(UserAgentValueProvider userAgentValueProvider) {
            this.userAgentValueProvider = userAgentValueProvider;
        }

        @Override
        public String getHeaderName() {
            return USER_AGENT;
        }

        @Override
        public String getHeaderValue() {
          return userAgentValueProvider.getHeaderValue();
        }

        public String getHeaderValue(String customPart) {
            return userAgentValueProvider.getHeaderValue(customPart);
        }

        private static UserAgentValueProvider loadValueProvider() {
            final Iterator<UserAgentValueProvider> customProviders =
                    ServiceLoader.load(UserAgentValueProvider.class, UserAgent.class.getClassLoader()).iterator();

            if (customProviders.hasNext()) {
                return customProviders.next();
            } else {
                return new UserAgentValueProvider.DEFAULT();
            }
        }
    }

    /**
     * Represent a name/value pair for a HTTP header. Not all are implemented. Only those used by the Heroku API.
     */
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

    /**
     * HTTP Methods. Not all are implemented. Only those used by the Heroku API.
     */
    public static enum Method {GET, PUT, POST, DELETE, PATCH}

    /**
     * HTTP Status codes. Not all are implemented. Only those used by the Heroku API.
     */
    public static enum Status {
        OK(200), CREATED(201), ACCEPTED(202), PAYMENT_REQUIRED(402), FORBIDDEN(403), NOT_FOUND(404), UNPROCESSABLE_ENTITY(422), INTERNAL_SERVER_ERROR(500), SERVICE_UNAVAILABLE(503);

        public final int statusCode;

        Status(int statusCode) {
            this.statusCode = statusCode;
        }

        public boolean equals(int code) {
            return statusCode == code;
        }
    }
}
