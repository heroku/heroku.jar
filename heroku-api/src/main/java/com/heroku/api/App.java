package com.heroku.api;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class App {

    String id;
    String name;
    String domain_name;
    String created_at;
    String create_status;
    String owner_email;
    String web_url;
    String stack;
    String requested_stack;
    String repo_migrate_status;
    String git_url;
    int slug_size;
	int repo_size;
    int dynos;
	int workers;

    public App named(String name) {
        App newApp = copy();
        newApp.name = name;
        return newApp;
    }

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

    private void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
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

    public String getName() {
        return name;
    }

    public String getDomainName() {
        return domain_name;
    }

    public String getWebUrl() {
        return web_url;
    }

    public String getGitUrl() {
        return git_url;
    }

    public String getCreateStatus() {
        return create_status;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getStack() {
        return stack;
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
