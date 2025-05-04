package com.aitpaeva.idoctor.service;

import com.aitpaeva.idoctor.exceptions.UserAlreadyExistsException;
import com.aitpaeva.idoctor.model.RefreshToken;
import com.aitpaeva.idoctor.model.User;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
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

}
