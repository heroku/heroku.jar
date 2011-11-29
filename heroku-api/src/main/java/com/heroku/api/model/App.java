package com.heroku.api.model;

import java.util.Date;

/**
 * TODO: Javadoc
 *
 * @author Naaman Newbold
 */
public class App {
    String id;
    String name;
    String create_status;
    String created_at;
    String stack;
    String requested_stack;
    String repo_migrate_status;
    int slug_size;
    int repo_size;
    int dynos;
    int workers;

    public void setWorkers(int workers) {
        this.workers = workers;
    }

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

}
