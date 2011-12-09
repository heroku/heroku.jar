Below is an experiment with creating an API that follows the following rules:

1. Verbs are mapped directly to Java methods
1. Resource requests (URI + body) are mapped to classes all inheriting for a top class
1. Requests are assembled strictly with method chaining
1. Generics are used to manage return types

This experiment should be compared to the current implementation and [this other experiment](design_experiment_2.md) where mapping is less strict.

# Bootstrap

    HerokuApi api = new HerokuApi(new HerokuApiConfig().setApiKey("mykey"));

# Addons

## List all available addons

    List<AddOn> l = api.list(AddOn.ALL);

## List addons installed on an app

    List<AddOn> l = api.list(App.name("myapp").addons());

## Install an addon to an app

    api.add(App.name("myapp").addon("redistogo").plan("nano"));

## Upgrade an addon on an app

    api.update(App.name("myapp").addon("redistogo").plan("mini"));

## Uninstall an addon from an app

    api.delete(App.name("myapp").addon("redistogo"));

# Apps

## List apps

    List<App> l = api.list(App.ALL,App.class);

## Get info for an app

    App myapp = api.get(App.name("myapp"),App.class);
    
## Create an app

Named app on default stack:

    api.add(App.name("myapp"));
    
Unnamed app on default stack:

    api.add(App.BLANK);

App on stack

    api.add(App.stack("cedar"))

Named app on stack

    api.add(App.name("myapp").stack("cedar"));

## Rename app

    api.update(App.name("myapp").newName("newappname"));

## Delete app

    api.delete(App.name("myapp"));

# Collaborators

## List collaborators on an app

    List<Collaborator> l = api.list(App.name("myapp").collaborators(),Collaborator.class);
    
## Add a collaborator to an app

    api.add(App.name("myapp").collaborator("jesper@heroku.com"));

## Remove a collaborator from an app

    api.delete(App.name("myapp").collaborator("jesper@heroku.com"));

# Config

## List config vars for an app

    List<Config> l = api.list(App.name("myapp").config(),Config.class);

## Add config vars to an app

Single config var

    api.add(App.name("myapp").config("key","value"));

Multiple config vars

    api.add(App.name("myapp")
            .config("key1","value1")
            .config("key2","config2"));

## Delete config var on an app

    api.delete(App.name("myapp").config("key"));

# Domains

## List domains for an app

    List<Domain> l = api.list(App.name("myapp").domains(),Domain.class);

## Add a domain to an app

    api.add(App.name("myapp").domain("www.mydomain.com"));

## Remove a domain from an app

    api.delete(App.name("myapp").domain("www.mydomain.com"));

# Keys

## List SSH keys

    List<PublicKey> l = api.list(User.current().keys(),PublicKey.class);

## Associate a key with this account

    api.add(User.current().key().value("keyvalue"));

## Remove a key from an account

    api.delete(User.current().key("keyname"));

## Remove all keys from an account

    api.delete(User.current().keys());

# Logs

## Get logs for an app

    URI logUri = api.get(App.name("myapp").logs()
                    .num(10)
                    .ps("web.1")
                    .source("router"),URI.class);

# Processes

Process commands are not really a good fit for this model. Need to think more about it.

## List processes for an app

    List<Process> l = api.list(App.name("myapp").ps(),Process.class);

## Run a one-off process in an app

    ProcessExecution p = api.add(App.name("myapp").processExecution()
                            .command("ls -l")
                            .attach(false), 
                            ProcessExecution.class);

## Restart a process of an app

    api.add(App.name("myapp").processRestart().type("web"));

## Stop a process of an app

    api.add(App.name("myapp").processStop().process("web.1"));
    
...

# Releases

## List releases for an app

## Get info for a release

## Rollback to a release

# Stacks

## List available stacks for an app

## Migrate an app to a new stack
