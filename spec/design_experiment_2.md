This is a second design experiment which should be compared to the current implementation and [this other experiment](design_experiment_1.md)

In the first experiment, we map the verb/noun structure of the REST API directly to Java by mapping verbs to methods and resources/nouns to classes. However, due to the clumpsy generic typing in Java this just doesn't look all that pretty. This experiment relaxes the strict mapping and instead goes with a fairly consistent pattern for naming methods and using classes. Here is the logic:

An operation on an entity is represented as a method call:

    api.<operation><entity>[plural]([appname],[requestobject | flattened arguments]);

with angular brackets meaning required, square brackets meaning optional. For example:

    api.listApps();

or

    api.createApp(new App("myapp").setStack("cedar"));

App name is excluded from the request class because it's present in almost all operations. Request class may be replaced by flat arguments if there is only one argument and/or if all arguments are required. E.g:

    api.upgradeAddOn("myapp","redistogo","mini");

By combining verb and noun in the method name we avoid generics like this:

    List<Collaborator> l = api.list(<some arguments>,Collaborator.class);

Word for word, a verb+entity method name is less verbose and also more correct. The generic version implies that you can pick your return type, but in reality it's tightly coupled to the other method arguments. Of course, the verb+noun approach results in a cartesian growth in methods. How bad is that? Because the Heroku API has a limited number of both verbs and nouns, it doesn't seem to be that bad. If number of nouns were unbounded, this approach obviously wouldn't work.

# Bootstrap

    HerokuApi api = new HerokuApi(new HerokuApiConfig().setApiKey("mykey"));

# Addons

## List all available addons

    List<AddOn> l = api.listAddOns();

## List addons installed on an app

    List<AddOn> l = api.listAddOns("myapp");

## Install an addon to an app

    api.addAddOn("myapp","redistogo","nano");

## Upgrade an addon on an app

    api.upgradeAddOn("myapp","redistogo","mini");

## Uninstall an addon from an app

    api.removeAddOn("myapp","redistogo");

# Apps

## List apps

    List<App> l = api.listApps();

## Get info for an app

    App myapp = api.getApp("myapp");
    
## Create an app

Named app on default stack:

    api.createApp(new App("myapp"));
    
Unnamed app on default stack:

    api.createApp(new App());

App on stack

    api.createApp(new App().setStack("cedar"))

Named app on stack

    api.createApp(new App("myapp").setStack("cedar"));

## Rename app

    api.updateApp("myapp",new App("newname"));

or

    api.renameApp("myapp","newappname");
    
## Delete app

    api.deleteApp("myapp");

(will confirm automatically)

# Collaborators

## List collaborators on an app

    List<Collaborator> l = api.listCollaborators("myapp");
    
## Add a collaborator to an app

    api.addCollaborator("myapp","jesper@heroku.com");

## Remove a collaborator from an app

    api.removeCollaborator("myapp","jesper@heroku.com");

# Config

## List config vars for an app

    List<Config> l = api.listConfig("myapp");

## Add config vars to an app

Single config var

    api.addConfig("myapp","key","value");

or

    api.addConfig("myapp", new Config("key","value"));

or

    api.addConfig("myapp", new ConfigSet().add("key","value"));
    
Multiple config vars

    api.addConfig("myapp", new ConfigSet()
            .add("key1","value1")
            .add("key2","config2"));

## Delete config var on an app

    api.removeConfig("myapp""key");

# Domains

## List domains for an app

    List<Domain> l = api.listDomains("myapp");

## Add a domain to an app

    api.addDomain("myapp","www.mydomain.com");

## Remove a domain from an app

    api.removeDomain("myapp","www.mydomain.com");

# Keys

## List SSH keys

    List<PublicKey> l = api.listPublicKeys();

## Associate a key with this account

    api.addPublicKey(new PublicKey().setValue("keystring"));

(because PublicKey has both a name and a value parameter, we want to make it clear what we pass)

## Remove a key from an account

    api.removePublicKey(new PublicKey().setName("keyname"));

## Remove all keys from an account

    api.removePublicKeys();

# Logs

## Get logs for an app

    URI logUri = api.getLog("myapp", new LogRequest()
                    .num(10)
                    .ps("web.1")
                    .source("router"));

# Processes

Process commands are not really a good fit for this model. Need to think more about it.

## List processes for an app

    List<Process> l = api.listProcesses("myapp");

## Run a one-off process in an app

    OneOffProcess p = api.startProcess("myapp", new OneOffProcessRequest()
                        .command("ls -l")
                        .attach(false));

## Restart a process of an app

a single process:

    api.restartProcess("myapp",new ProcessRequest().setName("web.1"));

all processes of a type:

    api.restartProcess("myapp",new ProcessRequest().setType("web"));
            
all processes

    api.restartProcesses("myapp");

or

    api.restartProcess("myapp", new ProcessRequest());

## Stop a process of an app

a single process:

    api.stopProcess("myapp",new ProcessRequest().setName("web.1"));

all processes of a type:

    api.stopProcess("myapp",new ProcessRequest().setType("web"));
            
all processes

    api.stopProcesses("myapp");

or

    api.stopProcess("myapp", new ProcessRequest());


## Scale a process for a Cedar app

    api.scaleProcess("myapp","web",2);

not using a request class here because 'type' and 'qty' are always required. But we could do:

    api.scaleProcess("myapp", new ScaleRequest().setType("web").setQuantity(2));

## Scale dynos for a Bamboo app

    api.scaleDynos("myapp",2);

## Scale workers for a Bamboo app

    api.scaleWorkers("myapp",2);

# Releases

## List releases for an app

    List<Release> l = api.listReleases("myapp");

## Get info for a release

    Release r = api.getRelease("myapp","v4");

## Rollback to a release

    api.rollbackRelease("myapp","v3");

# Stacks

## List available stacks for an app

    List<Stack> l = api.listStacks();

## Migrate an app to a new stack

    api.migrateToStack("myapp","bamboo");

