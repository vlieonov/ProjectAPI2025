package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.api.repo.RefreshTokenRepository;
import com.vlieonov.projectapi.api.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7);
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setTokenIsLive(false);
        userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(null);
        refreshTokenRepository.deleteByUserId(user.getId());
        return ResponseEntity.ok(true);
        }
        else {
            throw new IllegalArgumentException("Invalid authorization header");
        }
    }
}
