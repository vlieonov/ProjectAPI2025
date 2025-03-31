package com.vlieonov.projectapi.api.dto;

import com.vlieonov.projectapi.api.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GetUserInfo {
    private int id;
    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public GetUserInfo(int id, String username, String role, String email) {
        this.id = id;
        this.username = username;
        this.role = Role.valueOf(role);
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

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}