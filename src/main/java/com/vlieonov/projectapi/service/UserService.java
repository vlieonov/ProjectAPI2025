package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;

public interface UserService {
    public User getUser(int id);
    public String createUser(User user);
    public String updateUser(User user);
    public String deleteUser(int id);
    public String verify(User user);
}
