#Heroku JAR
The Heroku JAR is a java artifact that provides a simple wrapper for the Heroku REST API. The Heroku REST API allows Heroku users to manage their accounts, applications, addons, and other aspects related to Heroku.

##Usage
1. git clone git@github.com:heroku/heroku-jar.git
2. mvn install -DskipTests
3. Write some code:

    ```java
    String yourApiKey = ...
    HerokuAPI api = new HerokuAPI(new HttpClientConnection(yourApiKey));
    HerokuAppAPI appApi = HerokuAPI.newapp(Heroku.Stack.Cedar);
    ```



## Some Design Considerations

###Minimal Dependencies

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

###Flexible async model

Since we have decided to allow pluggable http client implementations, we also decided to allow asynchronous apis provided by the underlying httpclient
implementations to surface themselves in the API. The com.heroku.api.connection.Connection interface allows implementations to parameterize the type of
"Future" object they return from an async request. So for instance...

The provided implementation of Connection that uses apache httpclient `implements Connection<java.util.concurrent.Future>` and so
calls to executeCommandAsync will return a `<T extends CommandResponse> java.util.concurrent.Future<T>`

The provided implementation of Connection that uses twitter finagle `implements Connection<com.twitter.util.Future>` and so
calls to executeCommandAsync will return a `<T extends CommandResponse> com.twitter.util.Future<T>`, which has a much richer, composable api
than the java.util.concurrent.Future api.
