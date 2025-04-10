package com.vlieonov.projectapi.api.dto;

public class PassUpdate {
    private String oldPassword;
    private String newPassword;
    public PassUpdate(String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
