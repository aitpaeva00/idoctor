package com.aitpaeva.idoctor.service;

import com.aitpaeva.idoctor.model.RefreshToken;
import com.aitpaeva.idoctor.model.User;
import com.aitpaeva.idoctor.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;


    private final long EXPIRATION_TIME = 2 * 60 * 60 * 1000; // 2 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public RefreshToken createRefreshToken(User user) {

        RefreshToken existing = refreshTokenRepository.findByUser(user);
        if (existing != null) {
            refreshTokenRepository.delete(existing);
        }
        String tokenValue = generateRefreshToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(tokenValue);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60)); // 7 days

        return refreshTokenRepository.save(refreshToken);
    }

    public String refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue);
        if (refreshToken == null || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        return generateAccessToken(refreshToken.getUser());
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.findAll().stream()
            .filter(token -> token.getExpiryDate().isBefore(Instant.now()))
            .forEach(refreshTokenRepository::delete);
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 2 hours
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
