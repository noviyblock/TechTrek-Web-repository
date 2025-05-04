package com.startupgame.repository.auth;

import com.startupgame.entity.auth.RefreshToken;
import com.startupgame.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);
    Optional<RefreshToken> findByTokenValue(String token);
    void deleteByTokenValue(String refreshToken);
}
