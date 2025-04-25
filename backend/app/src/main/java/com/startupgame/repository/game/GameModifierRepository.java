package com.startupgame.repository.game;

import com.startupgame.entity.game.GameModifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameModifierRepository extends JpaRepository<GameModifier, Long> {
    List<GameModifier> findByGameId(Long gameId);
    Optional<GameModifier> findByGameIdAndModifierId(Long gameId, Long modifierId);
}
