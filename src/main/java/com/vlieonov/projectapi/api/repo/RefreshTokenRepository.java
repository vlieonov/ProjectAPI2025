package com.vlieonov.projectapi.api.repo;

import com.vlieonov.projectapi.api.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findById(Integer id);
    Optional<RefreshToken> findByToken(String token);
    void deleteById(Integer id);
}
