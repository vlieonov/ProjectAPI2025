package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String id) {
         return userRepository.findById(id).get();
    }

    @Override
    public String createUser(User user) {
        userRepository.save(user);
        return "1";
    }

    @Override
    public String updateUser(User user) {
        userRepository.save(user);
        return "1";
    }

    @Override
    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "1";
    }
}
