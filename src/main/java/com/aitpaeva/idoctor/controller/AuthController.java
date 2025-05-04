package com.aitpaeva.idoctor.controller;

import com.aitpaeva.idoctor.model.User;
import com.aitpaeva.idoctor.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Operation(summary = "Authenticate user and return access and refresh tokens")
    @PostMapping("/login")
    public  ResponseEntity<?>  login(@RequestBody Map<String, String> loginRequest) {
        try {
            Map<String, String> tokens = authService.login(loginRequest.get("username"), loginRequest.get("password"));
            return ResponseEntity.ok(tokens);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }    }

}
