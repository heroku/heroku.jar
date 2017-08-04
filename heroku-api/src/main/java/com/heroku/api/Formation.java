package com.heroku.api;

import java.io.Serializable;

/**
 * @author Román Sosa
 */
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    App app;
    String command;
    String created_at;
    String id;
    int quantity;
    String size;
    String type;
    String updated_at;
    
    public App getApp() {
        return app;
    }
    
    public void setApp(App app) {
        this.app = app;
    }
    
    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public String getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUpdated_at() {
        return updated_at;
    }
    
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
