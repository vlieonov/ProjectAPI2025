package com.vlieonov.projectapi.api.controller;

import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") int id) {
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
}

