# Heroku JAR
![License](https://img.shields.io/github/license/heroku/heroku.jar)
![Maven Central](https://img.shields.io/maven-central/v/com.heroku.api/heroku-api)
[![CI](https://github.com/heroku/heroku.jar/actions/workflows/ci.yml/badge.svg)](https://github.com/heroku/heroku.jar/actions/workflows/ci.yml)

The Heroku JAR is a java artifact that provides a simple wrapper for the Heroku REST API. The Heroku REST API allows Heroku users to manage their accounts, applications, addons, and other aspects related to Heroku.


## Usage

### Add Dependencies to your pom.xml

```xml
<dependency>
    <groupId>com.heroku.api</groupId>
    <artifactId>heroku-api</artifactId>
    <version>0.25</version>
</dependency>
<dependency>
    <groupId>com.heroku.api</groupId>
    <artifactId>heroku-json-jackson</artifactId>
    <version>0.25</version>
</dependency>
<dependency>
    <groupId>com.heroku.api</groupId>
    <artifactId>heroku-http-apache</artifactId>
    <version>0.25</version>
</dependency>
```

The artifacts are in Maven Central so you won't need to build them locally first if you don't want to.

### Use HerokuAPI
HerokuAPI contains all the methods necessary to interact with Heroku's REST API. HerokuAPI must be instantiated with an
API key in order to authenticate and make API calls. Requests to the API typically take no arguments, or simple strings.
Responses come in the form of read-only POJOs, Maps, or void.

```java
String apiKey = "...";
HerokuAPI api = new HerokuAPI(apiKey);
App app = api.createApp();
```


### API Key and Authentication
Heroku uses an API key for authentication. The API key can be found on the [account page](https://api.heroku.com/account).
API keys can be regenerated at any time by the user. Only the current API key shown on the account page will work. The API
key only changes when a user chooses to [regenerate](https://api.heroku.com/account) -- keys do not expire automatically.

[Basic Authentication](http://www.ietf.org/rfc/rfc2617.txt) over HTTPS is used for authentication. An empty username and
an API key are used to construct the Authorization HTTP header. HerokuAPI constructed with an API key, will handle
authentication for API requests.

When using API keys:
* If they need to be stored, store them securely (e.g. encrypt the file or database column).
* Catch RequestFailedException in case of an authorization failure.

### Examples

#### Instantiate HerokuAPI with an API Key
```java
String apiKey = "...";
HerokuAPI api = new HerokuAPI(apiKey);
```

#### Create an Application
```java
HerokuAPI api = new HerokuAPI(apiKey);
App app = api.createApp();
```

#### Create a named application on the cedar stack
```java
HerokuAPI api = new HerokuAPI(apiKey);
App app = api.createApp(new App().on(Heroku.Stack.Cedar).named("MyApp"));
```

#### List applications
```java
HerokuAPI api = new HerokuAPI(apiKey);
List<App> apps = api.listApps();
for (App app : apps) {
    System.out.println(app.getName());
}
```

#### Update config
```java
HerokuAPI api = new HerokuAPI(apiKey);
api.updateConfig("myExistingApp", new HashMap<String,String>(){{put("SOME_KEY", "SOMEVALUE")}});
```

#### Get Config
```java
HerokuAPI api = new HerokuAPI(apiKey);
Map<String, String> config = api.listConfig("myExistingApp");
for (Map.Entry<String, String> var : config.entrySet()) {
    System.out.println(var.getKey() + "=" + var.getValue());
}
```

#### Remove Config
The removeConfig call expects a single config var name to be removed.
```java
HerokuAPI api = new HerokuAPI(apiKey);
Map<String, String> config = api.removeConfig("myExistingApp", "configVarToRemove");
```

#### Overriding the User-Agent Header
The default User-Agent header is recommended for most use cases.

If this library is being used as part of another library or
application that wishes to set its own User-Agent header value,
implement the [`com.heroku.api.http.UserAgentValueProvider`](https://github.com/heroku/heroku.jar/blob/main/heroku-api/src/main/java/com/heroku/api/http/UserAgentValueProvider.java)
interface and create a provider-configuration file at `META-INF/services/com.heroku.api.http.UserAgentValueProvider`
containing the fully-qualified name of your provider class.
See [`java.util.ServiceLoader`](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) for details.

To conform to [RFC 2616 Section 14.43](http://tools.ietf.org/html/rfc2616#section-14.43), consider prepending the value
from the [`DEFAULT`](https://github.com/heroku/heroku.jar/blob/main/heroku-api/src/main/java/com/heroku/api/http/UserAgentValueProvider.java)
provider with your own user agent.

## Building Locally

1. Clone the repo:

        `git clone git@github.com:heroku/heroku-jar.git`

2. Build and install the jars:

    * Without running the tests:

            mvn install -DskipTests

    * Or run with tests:

            export HEROKU_TEST_USERS=[\{\"username\":\"defaultuser@example.com\",\"password\":\"defaultUserPass\",\"apikey\":\"defaultUserAPIKey\",\"defaultuser\":\"true\"\},\{\"username\":\"secondUser@example.com\",\"password\":\"password\",\"apikey\":\"apiKey\"\}]
            mvn install

## Continuous Integration

Tests are run automatically by Travis CI for all pushes and pull requests to `heroku/heroku.jar` with the exception of [pull requests from forks that only run unit tests](http://docs.travis-ci.com/user/pull-requests/#Security-Restrictions-when-testing-Pull-Requests). Release versions must be manually published as explained [below](#release).

## Release

To release a new version, run the release script:

```
$ bash release.sh
```

## Some Design Considerations

### Minimal Dependencies

One main design goal was to impose as few dependencies as possible on users of this api. Since there are
a wide range of target users for this library, from build tools to ide plugins to applications,
imposing an http client implementation or a json parsing implementation for users who are likely
already using (a different) one was undesireable.

To achieve this goal it was necessary to break down the structure of the project to allow users to configure the exceution of the api
in a way that dosent conflict with any dependencies in their project. Default implementations for http clients and json parsing are
available should the user of this api decide to use them.

As a conseqence, the dependency configuration for the api is slightly more verbose.

Instead of a short dependency declaration and inflexible implementation such as this, (maven style)

    <!--Not an actually available maven dependency-->
    <dependency>
       <groupId>com.heroku.api</groupId>
       <artifactId>heroku-api-impl-with-httpclient-and-gson</artifactId>
       <version>0.1-SNAPSHOT</version>
    </dependency>

We opted for a slightly more verbose dependency declaration an a flexible implementation, like this

     <dependency>
       <groupId>com.heroku.api</groupId>
       <artifactId>heroku-api</artifactId>
       <version>0.1-SNAPSHOT</version>
    </dependency>
        <dependency>
       <groupId>com.heroku.api</groupId>
       <artifactId>heroku-json-gson</artifactId>
       <version>0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
       <groupId>com.heroku.api</groupId>
       <artifactId>heroku-http-apache</artifactId>
       <version>0.1-SNAPSHOT</version>
    </dependency>

### Flexible async model

Since we have decided to allow pluggable http client implementations, we also decided to allow asynchronous apis provided by the underlying httpclient
implementations to surface themselves in the API. The com.heroku.api.connection.AsyncConnection interface allows implementations to parameterize the type of
"Future" object they return from an async request. So for instance...

The provided implementation of Connection that uses apache httpclient `implements AsyncConnection<java.util.concurrent.Future>` and so
calls to executeCommandAsync will return a `<T extends CommandResponse> java.util.concurrent.Future<T>`

The provided implementation of Connection that uses twitter finagle `implements AsyncConnection<com.twitter.util.Future>` and so
calls to executeCommandAsync will return a `<T extends CommandResponse> com.twitter.util.Future<T>`, which has a much richer, composable api
than the java.util.concurrent.Future api.
