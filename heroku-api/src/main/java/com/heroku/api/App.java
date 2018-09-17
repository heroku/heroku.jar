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
    App.Owner owner;
    String web_url;
    App.Stack stack;
    String requested_stack;
    String git_url;
    String buildpack_provided_description;
    String released_at;
    long slug_size;
    long repo_size;
    boolean maintenance;
    App.Space space;

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
        newApp.stack = new App.Stack(stack);
        return newApp;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
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

    private void setOwner(App.Owner owner) {
        this.owner = owner;
    }

    private void setStack(App.Stack stack) {
        this.stack = stack;
    }

    private void setRequested_stack(String requested_stack) {
        this.requested_stack = requested_stack;
    }

    private void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    private void setBuildpack_provided_description(String buildpack_provided_description) {
        this.buildpack_provided_description = buildpack_provided_description;
    }

    private void setSlug_size(long slug_size) {
        this.slug_size = slug_size;
    }

    private void setRepo_size(long repo_size) {
        this.repo_size = repo_size;
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

    public String getCreatedAt() {
        return created_at;
    }

    public App.Stack getStack() {
        return stack;
    }

    public String getRequestedStack() {
        return requested_stack;
    }

    public long getSlugSize() {
        return slug_size;
    }

    public long getRepoSize() {
        return repo_size;
    }

    public App.Owner getOwner() {
        return owner;
    }

    public String getReleasedAt(){
        return released_at;
    }

    public void setReleased_at(String at){
        released_at = at;
    }

    private App copy() {
        App copy = new App();
        copy.name = this.name;
        copy.stack = this.stack;
        return copy;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public static class Owner implements Serializable {

        private static final long serialVersionUID = 1L;

        String email;

        public Owner() {

        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class Stack implements Serializable {

        private static final long serialVersionUID = 1L;

        String id;
        String name;

        public Stack() {

        }

        public Stack(Heroku.Stack herokuStack) {
            this.name = herokuStack.value;
        }

        public Stack(String name) {
            this.name = name;
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
    }

    public static class Space implements Serializable {

        private static final long serialVersionUID = 1L;

        String id;
        String name;
        Boolean shield;

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

        public Boolean getShield() {
            return shield;
        }

        public void setShield(Boolean shield) {
            this.shield = shield;
        }
    }
}
