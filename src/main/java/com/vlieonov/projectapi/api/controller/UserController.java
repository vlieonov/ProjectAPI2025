package com.vlieonov.projectapi.api.controller;

import com.vlieonov.projectapi.api.model.GetUserInfo;
import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.owasp.encoder.Encode;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @RateLimiter(name = "loginRateLimiter")
    public String login(@RequestBody User user) {
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

   /* @GetMapping("/test")
    public String testResturn() {
        //return a execution script if spring security configuration is disable
        return """
      {
    "id": 1,
    "name": "<input type="text" value="test" onfocus="alert('XSS')" />
                         "
    }
      """;
    }*/
}

