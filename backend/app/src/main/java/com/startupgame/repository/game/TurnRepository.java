package com.startupgame.repository.game;

import com.startupgame.entity.game.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    Optional<Turn> findTopByGameIdOrderByTurnNumberDesc(Long gameId);
}
