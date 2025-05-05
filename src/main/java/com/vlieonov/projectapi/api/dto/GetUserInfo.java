package com.vlieonov.projectapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserInfo {
    private int id;
    private String username;
    private String email;
}