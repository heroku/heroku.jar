package com.heroku.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
@XmlRootElement(name = "app")
public class App {

    @XmlElement String id;
    @XmlElement String name;
    @XmlElement(name = "domain_name", type = Domain.class) Domain domain_name;
    @XmlElement String created_at;
    @XmlElement String create_status;
    @XmlElement(name = "owner") String owner_email;
    @XmlElement String web_url;
    @XmlElement String stack;
    @XmlElement String requested_stack;
    @XmlElement String repo_migrate_status;
    @XmlElement String git_url;
    @XmlElement int slug_size;
	@XmlElement int repo_size;
    @XmlElement int dynos;
	@XmlElement int workers;

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

    public Domain getDomain() {
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
