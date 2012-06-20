package com.heroku.api;

import java.io.Serializable;

/**
 * Data model for a Heroku App. Also serves as a builder class when making requests to create an app.
 *
 * @author Naaman Newbold
 */
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    String name;
    Domain domain_name;
    String created_at;
    String create_status;
    String owner_email;
    String web_url;
    String stack;
    String requested_stack;
    String repo_migrate_status;
    String git_url;
    String buildpack_provided_description;
    int slug_size;
	int repo_size;
    int dynos;
	int workers;

    /**
     * Builder method for specifying the name of an app.
     * @param name The name to give an app.
     * @return A copy of the {@link App}
     */
    public App named(String name) {
        App newApp = copy();
        newApp.name = name;
        return newApp;
    }

    /**
     * Builder method for specifying the stack an app should be created on.
     * @param stack Stack to create the app on.
     * @return A copy of the {@link App}
     */
    public App on(Heroku.Stack stack) {
        App newApp = copy();
        newApp.stack = stack.toString();
        return newApp;
    }

    private void setweb_url(String webUrl) {
        web_url = webUrl;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setDomain(Domain domain) {
        this.domain_name = domain;
    }

    private void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private void setCreate_status(String create_status) {
        this.create_status = create_status;
    }

    private void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    private void setStack(String stack) {
        this.stack = stack;
    }

    private void setRequested_stack(String requested_stack) {
        this.requested_stack = requested_stack;
    }

    private void setRepo_migrate_status(String repo_migrate_status) {
        this.repo_migrate_status = repo_migrate_status;
    }

    private void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    private void setBuildpack_provided_description(String buildpack_provided_description) {
        this.buildpack_provided_description = buildpack_provided_description;
    }

    private void setSlug_size(int slug_size) {
        this.slug_size = slug_size;
    }

    private void setRepo_size(int repo_size) {
        this.repo_size = repo_size;
    }

    private void setDynos(int dynos) {
        this.dynos = dynos;
    }

    private void setWorkers(int workers) {
        this.workers = workers;
    }

    public String getId() {
        return id;
    }

    public void setDomain_name(Domain domain_name) {
        this.domain_name = domain_name;
    }

    public String getName() {

        return name;
    }

    public Domain getDomain() {
        return domain_name;
    }

    public String getWebUrl() {
        return web_url;
    }



    public String getGitUrl() {
        return git_url;
    }

    public String getBuildpackProvidedDescription() {
        return buildpack_provided_description;
    }

    public String getCreateStatus() {
        return create_status;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public Heroku.Stack getStack() {
        return Heroku.Stack.fromString(stack);
    }

    public String getRequestedStack() {
        return requested_stack;
    }

    public String getRepoMigrateStatus() {
        return repo_migrate_status;
    }

    public int getSlugSize() {
        return slug_size;
    }

    public int getRepoSize() {
        return repo_size;
    }

    public int getDynos() {
        return dynos;
    }

    public int getWorkers() {
        return workers;
    }

    public String getOwnerEmail() {
        return owner_email;
    }

    private App copy() {
        App copy = new App();
        copy.name = this.name;
        copy.stack = this.stack;
        return copy;
    }
    
}
