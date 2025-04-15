package com.vlieonov.projectapi.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_data")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String role;
    private String email;
    private String password;
    private boolean tokenIsLive;
    public User() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return username;
    }
    public void setName(String name) {
        this.username = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isTokenIsLive() {
        return tokenIsLive;
    }
    public void setTokenIsLive(boolean tokenIsLive) {
        this.tokenIsLive = tokenIsLive;
    }

}
