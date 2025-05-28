package com.startupgame.modules.auth.repository;

import com.startupgame.modules.auth.entity.RefreshToken;
import com.startupgame.modules.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);
    Optional<RefreshToken> findByTokenValue(String token);
    void deleteByTokenValue(String refreshToken);
}
