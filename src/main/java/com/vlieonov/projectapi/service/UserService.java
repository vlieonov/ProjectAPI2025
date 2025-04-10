package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.dto.GetUserInfo;
import com.vlieonov.projectapi.api.dto.JwtResponse;
import com.vlieonov.projectapi.api.model.RefreshToken;
import com.vlieonov.projectapi.api.model.User;

public interface UserService {
    public GetUserInfo getUser(int id);
    public String createUser(User user);
    public String deleteUser(int id);
    public JwtResponse verify(User user);
    public JwtResponse refresh(RefreshToken refreshToken);
    public GetUserInfo profile(String username);
    public void emailCheck(String email);
    public void passwordCheck(String password);
    public void updatePassword(String username, String oldPassword, String newPassword);
}
