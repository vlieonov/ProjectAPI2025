package com.vlieonov.projectapi.api.controller;

import com.vlieonov.projectapi.api.dto.GetUserInfo;
import com.vlieonov.projectapi.api.dto.JwtResponse;
import com.vlieonov.projectapi.api.dto.PassUpdate;
import com.vlieonov.projectapi.api.model.RefreshToken;
import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.api.repo.UserRepository;
import com.vlieonov.projectapi.service.LogoutService;
import com.vlieonov.projectapi.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.owasp.encoder.Encode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final LogoutService logoutService;
    private UserService userService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, LogoutService logoutService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.logoutService = logoutService;
    }

    @PostMapping("/login")
    @RateLimiter(name = "RateLimiter")
    public JwtResponse login(@RequestBody User user) {
        User userCopy = userRepository.findByUsername(user.getUsername());
        userCopy.setTokenIsLive(true);
        userRepository.save(userCopy);
        return userService.verify(user);
    }

    @GetMapping("{id}")
    public GetUserInfo getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "User deleted";
    }

    @PostMapping("/register")
    @RateLimiter(name = "RateLimiter")
    public String registerUser(@RequestBody User user) {
        userService.passwordCheck(user.getPassword());
        userService.createUser(user);
        return "User created";
    }

    @PostMapping("/comment")
    @RateLimiter(name = "CommentRateLimiter")
    public String addComment(@RequestBody String comment) {
        String decodedComment = URLDecoder.decode(comment, StandardCharsets.UTF_8);
        String safeComment = Jsoup.clean(decodedComment, Safelist.none());
        String encodedComment = Encode.forHtml(safeComment);
        if (encodedComment.isEmpty()) {
            throw new RuntimeException("Comment is empty");
        }
        return "Your comment: " + encodedComment;
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshToken refreshToken) {
        return userService.refresh(refreshToken);
    }

    @GetMapping("/profile")
    public GetUserInfo getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.profile(username);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        return logoutService.logout(request);
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody PassUpdate passUpdate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(username, passUpdate.getOldPassword(), passUpdate.getNewPassword());
        return "Password updated";
    }
}

