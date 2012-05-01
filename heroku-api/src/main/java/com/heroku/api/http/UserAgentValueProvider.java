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
 * @see java.util.ServiceLoader
 * @see Http.UserAgent
 *
 * @author Ryan Brainard
*/
public interface UserAgentValueProvider {

    String getHeaderValue();
    String getHeaderValue(String customPart);

    class DEFAULT implements UserAgentValueProvider {
        private final String userAgentValuePattern = "heroku.jar-%s-v%s";

        @Override
        public String getHeaderValue() {
            return getHeaderValue("unspecified");
        }

        @Override
        public String getHeaderValue(String customPart) {
            return String.format(userAgentValuePattern, customPart, Heroku.JarProperties.getProperty("heroku.jar.version"));
        }
    }
}
