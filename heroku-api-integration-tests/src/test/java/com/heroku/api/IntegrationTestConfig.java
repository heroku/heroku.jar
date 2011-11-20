package com.heroku.api;


public enum IntegrationTestConfig {

    USER("HEROKU_USER", "heroku.user"),
    PASSWORD("HEROKU_PASSWORD", "heroku.password"),
    APIKEY("HEROKU_APIKEY", "heroku.apikey"),
    TEST_AGAINST_PRODUCTION("HEROKU_TEST_PRODUCTION", "heroku.test.production");

    private String environmentVariable;
    private String systemProperty;

    IntegrationTestConfig(String envvar, String sysprop) {
        this.environmentVariable = envvar;
        this.systemProperty = sysprop;
    }

    public String getRequiredConfig() {
        String value = System.getProperty(systemProperty, System.getenv(environmentVariable));
        if (value == null) {
            throw new IllegalStateException(String.format("Either environment variable %s or system property %s must be defined", environmentVariable, systemProperty));
        }
        return value;
    }

    public String getOptionalConfig() {
        return System.getProperty(systemProperty, System.getenv(environmentVariable));
    }


}
