package com.vlieonov.projectapi.api.model;

public class GetUserInfo {
    private int id;
    private String username;
    private String email;

    public GetUserInfo(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}