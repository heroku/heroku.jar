#Heroku JAR
The Heroku JAR is a java artifact that provides a simple wrapper for the Heroku REST API. The Heroku REST API allows Heroku users to manage their accounts, applications, addons, and other aspects related to Heroku.

##Usage
1. git clone git@github.com:heroku/heroku-jar.git
2. mvn install -DskipTests
3. Write some code:

    ```java
    Connection<?> connection = new HttpClientConnection(new BasicAuthLoginCommand('username', 'password'));
    HerokuAPI api = new HerokuAPI(connection);
    HerokuAppAPI appApi = HerokuAPI.newapp(HerokuStack.Cedar);
    ```
    