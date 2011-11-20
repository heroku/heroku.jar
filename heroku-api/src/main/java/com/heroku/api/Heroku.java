package com.heroku.api;


import com.heroku.api.exception.HerokuAPIException;
import com.heroku.api.http.Http;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class Heroku {


    public enum Config {
        ENDPOINT("HEROKU_HOST", "heroku.host", "https://api.heroku.com");
        public final String environmentVariable;
        public final String systemProperty;
        public final String defaultValue;
        public final String value;

        Config(String environmentVariable, String systemProperty, String defaultValue) {
            this.environmentVariable = environmentVariable;
            this.systemProperty = systemProperty;
            this.defaultValue = defaultValue;
            String envVal = System.getenv(environmentVariable);
            this.value = System.getProperty(systemProperty, envVal == null ? defaultValue : envVal);
        }

        public boolean isDefault() {
            return defaultValue.equals(value);
        }

    }

    public static SSLContext herokuSSLContext() {
        return sslContext(Config.ENDPOINT.isDefault());
    }

    public static SSLContext sslContext(boolean verify) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            TrustManager[] tmgrs = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).getTrustManagers();
            if (!verify) {
                tmgrs = trustAllTrustManagers();
            }
            KeyManager[] kmgrs = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).getKeyManagers();
            if (!verify) {
                kmgrs = null;
            }
            ctx.init(kmgrs, tmgrs, new SecureRandom());
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


    public static enum RequestKey {
        stack("app[stack]"),
        createAppName("app[name]"),
        remote("remote"),
        timeout("timeout"),
        addons("addons"),
        addonName("addon"),
        requested("requested"),
        beta("beta"),
        appName("name"),
        sshkey("sshkey"),
        config("config"),
        collaborator("collaborator[email]"),
        transferOwner("app[transfer_owner]"),
        configvars("config_vars"),
        configVarName("key"),
        processType("type"),
        processName("ps"),
        quantity("qty"),
        username("username"),
        password("password");

        public final String queryParameter;

        RequestKey(String queryParameter) {
            this.queryParameter = queryParameter;
        }
    }


    public static enum Stack {
        Aspen("aspen"),
        Bamboo("bamboo"),
        Cedar("cedar");

        public final String value;

        Stack(String value) {
            this.value = value;
        }
    }


    public static enum Resource {
        Login("/login"),
        Apps("/apps"),
        App("/apps/%s"),
        Addons("/addons"),
        AppAddons(App.value + "/addons"),
        AppAddon(AppAddons.value + "/%s"),
        User("/user"),
        Key(User.value + "/keys/%s"),
        Keys(User.value + "/keys"),
        Collaborators(App.value + "/collaborators"),
        Collaborator(Collaborators.value + "/%s"),
        ConfigVars(App.value + "/config_vars"),
        ConfigVar(ConfigVars.value + "/%s"),
        Logs(App.value + "/logs"),
        Process(App.value + "/ps"),
        Restart(Process.value + "/restart"),
        Stop(Process.value + "/stop"),
        Scale(Process.value + "/scale");

        public final String value;

        Resource(String value) {
            this.value = value;
        }
    }

    public static enum ApiVersion implements Http.Header {

        v2(2), v3(3);

        public static final String HEADER = "X-Heroku-API-Version";

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
            return Integer.toString(version);
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
