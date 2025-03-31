package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.RefreshToken;
import com.vlieonov.projectapi.api.repo.RefreshTokenRepository;
import com.vlieonov.projectapi.api.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void clearRefreshTokensOnStartup() {
        refreshTokenRepository.deleteAll();
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expireDate(Instant.now().plusMillis(600000 * 10))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken IsExpired(RefreshToken token) {
        if (token.getExpireDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Expired token");
        }
        else return token;
    }
}
