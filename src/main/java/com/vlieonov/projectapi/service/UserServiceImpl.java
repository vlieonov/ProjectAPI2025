package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.dto.GetUserInfo;
import com.vlieonov.projectapi.api.dto.JwtResponse;
import com.vlieonov.projectapi.api.model.RefreshToken;
import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.api.repo.RefreshTokenRepository;
import com.vlieonov.projectapi.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final AuthenticationManager authManager;
    UserRepository userRepository;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserInfo getUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return new GetUserInfo(user.getId(), user.getName(), user.getRole(), user.getEmail());
    }

    @Override
    public String createUser(User user) {
        user.setRole("USER");
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
    public JwtResponse verify(User user) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (auth.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(user.getName()))
                    .token(refreshToken.getToken())
                    .build();
        }
        else throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public JwtResponse refresh(RefreshToken refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken.getToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        refreshTokenService.IsExpired(storedToken);

        User user = storedToken.getUser();
        if (user == null) {
            throw new BadCredentialsException("User not found for this token");
        }

        return JwtResponse.builder()
                .accessToken(jwtService.generateToken(user.getName()))
                .token(refreshToken.getToken())
                .build();
    }

}
