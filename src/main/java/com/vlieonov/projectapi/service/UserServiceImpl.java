package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService{
    private final AuthenticationManager authManager;
    UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    @Override
    public String createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User created";
    }

    @Override
    public String updateUser(User user) {
        userRepository.save(user);
        return "1";
    }

    @Override
    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "1";
    }

    @Override
    public String verify(User user) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (auth.isAuthenticated()) return jwtService.generateToken(user.getName());
        else return "Failure";
    }
}
