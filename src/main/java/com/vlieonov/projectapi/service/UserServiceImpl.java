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
        if(userRepository.findByUsername(user.getName()) != null)
            throw new IllegalArgumentException("Username already exists");
        emailCheck(user.getEmail());
        passwordCheck(user.getPassword());
        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User created";
    }

    @Override
    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "Success";
    }
    @Override
    public JwtResponse verify(User user) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        if (auth.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(user))
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
                .accessToken(jwtService.generateToken(user))
                .token(refreshToken.getToken())
                .build();
    }

    @Override
    public GetUserInfo profile(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return new GetUserInfo(user.getId(), user.getName(), user.getRole(), user.getEmail());
    }

    @Override
    public void emailCheck(String email) {
        if(!email.contains("@")) throw new BadCredentialsException("Invalid email");
    }

    @Override
    public void passwordCheck(String password) {
        if(password.length() < 8) throw new BadCredentialsException("Password must be at least 8 characters");
        String specialChar = ".*[~!@#$%^&*()\\-_=+\\[{\\]}|;:<>,./?].*";
        if (!password.matches(specialChar)) throw new BadCredentialsException("Password does not match");
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if(encoder.matches(oldPassword, user.getPassword())) {
            passwordCheck(newPassword);
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        }
        else throw new BadCredentialsException("Password does not match");
    }
}
