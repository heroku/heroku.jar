package com.heroku.api;


import com.heroku.api.parser.JsonSelector;
import com.heroku.api.parser.Parser;
import com.heroku.api.parser.TypeReference;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public enum IntegrationTestConfig {

    /**
     * A JSON array of users to be used in testing. A default user must be specified with the attribute
     * default:true.
     * <p/>
     * e.g.
     * <code>
     * [ {"username":"username@heroku.com","password":"password","apikey":"apikey","default":"true"},
     * {"username":"username2@heroku.com","password":"password2","apikey":"apikey2"},
     * {"username":"username@heroku.com3","password":"password3","apikey":"apikey3"},
     * ...
     * ]
     * </code>
     * <p/>
     * In many cases, special characters need to be escaped. For example, when setting an environment variable
     * in Linux:
     * <code>
     * export HEROKU_TEST_USERS=[\{\"username\":\"username@heroku.com\",\"password\":\"password\",\"apikey\":\"apikey\",\"defaultuser\":\"true\"\} ...
     * </code>
     */
    CONFIG("HEROKU_TEST_USERS", "heroku.test.users");

    private String environmentVariable;
    private String systemProperty;
    private List<TestUser> testUsers;
    private TestUser defaultUser;
    private List<TestUser> otherUsers;

    IntegrationTestConfig(String envvar, String sysprop) {
        this.environmentVariable = envvar;
        this.systemProperty = sysprop;
    }

    public TestUser getDefaultUser() {
        if (defaultUser == null) {
            loadUsers();
        }

        return defaultUser;
    }

    /**
      * Gets a list of all users not marked as default:true.
      */
    public List<TestUser> getOtherUsers() {
      if (otherUsers == null) {
        loadUsers();
      }

      return otherUsers;
    }

    /**
     * Gets a user that is not the default user.
     */
    public TestUser getOtherUser() {
        if (otherUsers == null) {
          loadUsers();
        }

        return otherUsers.get(0);
    }
    public List<TestUser> getTestUsers() {
        if (testUsers == null) {
            loadUsers();
        }

        return testUsers;
    }

    private void loadUsers() {
        assertConfigIsPresent();
        Parser jsonParser = JsonSelector.getParser();
        testUsers = jsonParser.parse(getConfig().getBytes(), new TypeReference<List<TestUser>>() {
        }.getType());
        otherUsers = new ArrayList<TestUser>();

        for (TestUser tu : testUsers) {
            if (tu.isDefaultUser()) {
                defaultUser = tu;
            } else {
                otherUsers.add(tu);
            }
        }

        assertDefaultUserIsPresent();
    }

    private void assertDefaultUserIsPresent() {
        assertNotNull(defaultUser, "A default user must be specified in the list of users.");
    }

    private void assertConfigIsPresent() {
        assertNotNull(
                getConfig(),
                String.format(
                        "Either environment variable %s or system property %s must be defined",
                        environmentVariable,
                        systemProperty
                )
        );
    }

    private String getConfig() {
        return System.getProperty(systemProperty, System.getenv(environmentVariable));
    }

    public static class TestUser {
        boolean defaultuser;
        String username;
        String password;
        String apikey;

        private void setDefaultuser(boolean defaultUser) {
            this.defaultuser = defaultUser;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        private void setPassword(String password) {
            this.password = password;
        }

        private void setapikey(String apiKey) {
            this.apikey = apiKey;
        }

        public boolean isDefaultUser() {
            return defaultuser;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getApiKey() {
            return apikey;
        }
    }
}
