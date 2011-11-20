package com.heroku.api;


import com.heroku.api.http.Http;

public class Heroku {

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
}
