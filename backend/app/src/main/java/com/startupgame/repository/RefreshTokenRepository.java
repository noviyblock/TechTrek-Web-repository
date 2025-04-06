package com.startupgame.repository;

import com.startupgame.entity.RefreshToken;
import com.startupgame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);
    Optional<RefreshToken> findByTokenValue(String token);
}
