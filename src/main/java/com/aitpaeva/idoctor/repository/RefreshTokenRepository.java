package com.aitpaeva.idoctor.repository;

import com.aitpaeva.idoctor.model.RefreshToken;
import com.aitpaeva.idoctor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
    void deleteByUser(User user);
    RefreshToken findByUser(User user);
}
