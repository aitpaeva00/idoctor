package com.aitpaeva.idoctor.controller;

import com.aitpaeva.idoctor.model.User;
import com.aitpaeva.idoctor.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @Operation(summary = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String token = authService.login(loginRequest.get("username"), loginRequest.get("password"));
        return Map.of("token", token);
    }
}
