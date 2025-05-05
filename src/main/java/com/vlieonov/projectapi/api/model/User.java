package com.vlieonov.projectapi.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_data")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;
    private boolean tokenIsLive;
}
