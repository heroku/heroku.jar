package com.heroku.api.http;

import com.heroku.api.Heroku;

/**
 * Provider for User-Agent header values used by HTTP clients.
 *
 * To provide a custom User-Agent value, implement this interface
 * and create a <i>provider-configuration file</i> at
 * <tt>META-INF/services/com.heroku.api.http.UserAgentValueProvider</tt>
 * containing the fully-qualified name of your provider class.
 * See {@link java.util.ServiceLoader} for details.
 * 
 * To conform to RFC 2616 Section 14.43, consider prepending the value
 * from the {@link DEFAULT} provider with your own user agent.
 *
 * @see java.util.ServiceLoader
 * @see Http.UserAgent
 *
 * @author Ryan Brainard
*/
public interface UserAgentValueProvider {

    String getHeaderValue();
    String getHeaderValue(String customPart);

    class DEFAULT implements UserAgentValueProvider {
        private final String userAgentValuePattern = "heroku.jar/%s (%s) Java/%s (%s)";

        @Override
        public String getHeaderValue() {
            return getHeaderValue("");
        }

        @Override
        public String getHeaderValue(String subComponentUserAgents) {
            return String.format(userAgentValuePattern,
                Heroku.JarProperties.getProperty("heroku.jar.version"),
                subComponentUserAgents,
                System.getProperty("java.version"),
                System.getProperty("java.vendor"));
        }
    }
}
