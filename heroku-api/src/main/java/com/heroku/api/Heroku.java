package com.heroku.api;


import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.Http;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Heroku {


    public enum Config {
        ENDPOINT("HEROKU_HOST", "heroku.host", "heroku.com");
        public final String environmentVariable;
        public final String systemProperty;
        public final String defaultValue;
        public final String value;

        Config(String environmentVariable, String systemProperty, String defaultValue) {
            this.environmentVariable = environmentVariable;
            this.systemProperty = systemProperty;
            this.defaultValue = defaultValue;
            String envVal = System.getenv(environmentVariable);
            String configVal = System.getProperty(systemProperty, envVal == null ? defaultValue : envVal);
            this.value = configVal.matches("^https?:\\/\\/.*") ? configVal : "https://api." + configVal;
        }

        public boolean isDefault() {
            return defaultValue.equals(value);
        }

    }

    public static enum JarProperties {
        ;

        static final Properties properties = new Properties();

        static {
            try {
              InputStream jarProps = JarProperties.class.getResourceAsStream("/heroku.jar.properties");
              properties.load(jarProps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public static String getProperty(String propName) {
            return properties.getProperty(propName);
        }

        public static Properties getProperties() {
            return properties;
        }
    }

    public static SSLContext herokuSSLContext() {
        return sslContext(Config.ENDPOINT.isDefault());
    }

    public static SSLContext sslContext(boolean verify) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            TrustManager[] tmgrs = null;
            if (!verify) {
                tmgrs = trustAllTrustManagers();
            }
            /*
Initializes this context.
Either of the first two parameters may be null in which case the installed security providers will be searched
for the highest priority implementation of the appropriate factory.
Likewise, the secure random parameter may be null in which case the default implementation will be used.
            */
            ctx.init(null, tmgrs, null);
            return ctx;
        } catch (NoSuchAlgorithmException e) {
            throw new HerokuAPIException("NoSuchAlgorithmException while trying to setup SSLContext", e);
        } catch (KeyManagementException e) {
            throw new HerokuAPIException("KeyManagementException while trying to setup SSLContext", e);
        }
    }

    public static HostnameVerifier hostnameVerifier(boolean verify) {
        HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
        if (!verify) {
            verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
        }
        return verifier;
    }

    public static HostnameVerifier herokuHostnameVerifier() {
        return hostnameVerifier(Config.ENDPOINT.isDefault());
    }

    public static enum ResponseKey {
        Name("name"),
        DomainName("domain_name"),
        CreateStatus("create_status"),
        Stack("stack"),
        SlugSize("slug_size"),
        RequestedStack("requested_stack"),
        CreatedAt("created_at"),
        WebUrl("web_url"),
        RepoMigrateStatus("repo_migrate_status"),
        Id("id"),
        GitUrl("git_url"),
        RepoSize("repo_size"),
        Dynos("dynos"),
        Workers("workers");

        public final String value;
        
        // From Effective Java, Second Edition
        private static final Map<String, ResponseKey> stringToResponseKey = new HashMap<String, ResponseKey>();
        static {
            for (ResponseKey key : values())
                stringToResponseKey.put(key.toString(), key);
        }
        
        ResponseKey(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
        
        public static ResponseKey fromString(String keyName) {
            return stringToResponseKey.get(keyName);
        }
    }

    public static enum RequestKey {
        Slug("slug"),
        StackName("name"),
        Stack("stack"),
        AppMaintenance("maintenance"),
        AddonName("addon"),
        AddonPlan("plan"),
        AppName("name"),
        SSHKey("public_key"),
        Collaborator("user"),
        TransferAppName("app"),
        TransferOwner("recipient"),
        Release("release"),
        CreateDomain("hostname"),
        DeleteDomain("hostname"),
        ProcessTypes("process_types"),
        Dyno("dyno"),
        LogLines("lines"),
        LogSource("source"),
        LogTail("tail");

        public final String queryParameter;

        RequestKey(String queryParameter) {
            this.queryParameter = queryParameter;
        }
    }


    public static enum Stack {
        Cedar14("cedar-14"),
        Cedar16("cedar-16");

        public final String value;

        // From Effective Java, Second Edition
        private static final Map<String, Stack> stringToEnum = new HashMap<String, Stack>();
        static {
            for (Stack s : values())
                stringToEnum.put(s.toString(), s);
        }

        Stack(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Stack fromString(String stackName) {
            return stringToEnum.get(stackName);
        }
    }


    public static enum Resource {
        Apps("/apps"),
        App("/apps/%s"),
        AppTransfer("/account/app-transfers"),
        Addons("/addons"),
        AppAddons(App.value + "/addons"),
        AppAddon(AppAddons.value + "/%s"),
        User("/account"),
        Key(User.value + "/keys/%s"),
        Keys(User.value + "/keys"),
        Collaborators(App.value + "/collaborators"),
        Collaborator(Collaborators.value + "/%s"),
        ConfigVars(App.value + "/config-vars"),
        Logs(App.value + "/log-sessions"),
        Process(App.value + "/ps"),
        Restart(Process.value + "/restart"),
        Stop(Process.value + "/stop"),
        Scale(Process.value + "/scale"),
        Releases(App.value + "/releases"),
        Release(Releases.value + "/%s"),
        Slugs(App.value + "/slugs"),
        Slug(Slugs.value + "/%s"),
        Status(App.value + "/status"),
        Stacks("/stacks"),
        Domains(App.value + "/domains"),
        Domain(Domains.value + "/%s");

        public final String value;

        Resource(String value) {
            this.value = value;
        }

        public String format(String... values) {
            return String.format(value, values);
        }
    }

    public enum ApiVersion implements Http.Header {

        v2(2), v3(3);

        public static final String HEADER = "Accept";

        public final int version;

        ApiVersion(int version) {
            this.version = version;
        }

        @Override
        public String getHeaderName() {
            return HEADER;
        }

        @Override
        public String getHeaderValue() {
            return "application/vnd.heroku+json; version=" + Integer.toString(version);
        }
    }

    public static TrustManager[] trustAllTrustManagers() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
    }
}
