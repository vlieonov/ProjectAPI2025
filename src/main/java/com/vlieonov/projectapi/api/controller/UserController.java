package com.vlieonov.projectapi.api.controller;

import com.vlieonov.projectapi.api.dto.GetUserInfo;
import com.vlieonov.projectapi.api.dto.JwtResponse;
import com.vlieonov.projectapi.api.model.RefreshToken;
import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.api.repo.UserRepository;
import com.vlieonov.projectapi.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.owasp.encoder.Encode;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private UserService userService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @RateLimiter(name = "loginRateLimiter")
    public JwtResponse login(@RequestBody User user) {
        return userService.verify(user);
    }

    @GetMapping("{id}")
    public GetUserInfo getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }
    @PostMapping
    public String createUser(@RequestBody User user) {
        userService.createUser(user);
        return "User created";
    }
    @PutMapping
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "User updated";
    }
    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "User deleted";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.createUser(user);
        return "User created";
    }

    @PostMapping("/comment")
    public String addComment(@RequestBody String comment) {
        String encodedComment = Encode.forHtmlUnquotedAttribute(comment);
        return "Your comment: " + encodedComment;
    }
    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshToken refreshToken) {
        return userService.refresh(refreshToken);
    }
}

