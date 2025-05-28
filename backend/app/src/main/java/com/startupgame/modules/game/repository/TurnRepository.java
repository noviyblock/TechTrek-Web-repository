package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    Optional<Turn> findTopByGameIdOrderByTurnNumberDesc(Long gameId);
    List<Turn> findAllByGameId(Long gameId);
}
