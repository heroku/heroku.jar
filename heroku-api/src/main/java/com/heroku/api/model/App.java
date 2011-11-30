package com.heroku.api.model;

import java.util.Date;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class App {

    String id, name, domain_name, created_at, create_status;
    String web_url, stack, requested_stack, repo_migrate_status;
    String git_url;
    int slug_size, repo_size, dynos, workers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }
    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
    public String getGit_url() {
        return git_url;
    }

    public void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    public String getCreate_status() {
        return create_status;
    }

    public void setCreate_status(String create_status) {
        this.create_status = create_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getRequested_stack() {
        return requested_stack;
    }

    public void setRequested_stack(String requested_stack) {
        this.requested_stack = requested_stack;
    }

    public String getRepo_migrate_status() {
        return repo_migrate_status;
    }

    public void setRepo_migrate_status(String repo_migrate_status) {
        this.repo_migrate_status = repo_migrate_status;
    }

    public int getSlug_size() {
        return slug_size;
    }

    public void setSlug_size(int slug_size) {
        this.slug_size = slug_size;
    }

    public int getRepo_size() {
        return repo_size;
    }

    public void setRepo_size(int repo_size) {
        this.repo_size = repo_size;
    }

    public int getDynos() {
        return dynos;
    }

    public void setDynos(int dynos) {
        this.dynos = dynos;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

}
