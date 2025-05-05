package com.aitpaeva.idoctor.service;

import com.aitpaeva.idoctor.exceptions.UserAlreadyExistsException;
import com.aitpaeva.idoctor.model.RefreshToken;
import com.aitpaeva.idoctor.model.User;
import com.aitpaeva.idoctor.repository.RefreshTokenRepository;
import com.aitpaeva.idoctor.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String register(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    public Map<String, String> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String accessToken = tokenService.generateAccessToken(user); // short-lived token
                RefreshToken refreshToken = tokenService.createRefreshToken(user); // long-lived token

                return Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken.getToken()
                );
            }
        }
        throw new BadCredentialsException("Invalid credentials!");
    }

    public void deleteUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Удалить refresh token, если есть
        RefreshToken token = refreshTokenRepository.findByUser(user);
        if (token != null) {
            refreshTokenRepository.delete(token);
        }

        userRepository.delete(user);
    }



}
