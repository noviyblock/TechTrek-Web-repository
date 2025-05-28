package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.GameFinalScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameFinalScoreRepository extends JpaRepository<GameFinalScore, Long> {

    Optional<GameFinalScore> findByGameId(Long gameId);
}