package com.academy.fintech.core.db.refresh.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(UUID userId);
    RefreshToken findByToken(String token);
}
