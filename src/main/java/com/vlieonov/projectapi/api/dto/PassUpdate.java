package com.vlieonov.projectapi.api.dto;

import lombok.Data;

@Data
public class PassUpdate {
    private String oldPassword;
    private String newPassword;
}
