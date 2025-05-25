package com.startupgame.repository.game;

import com.startupgame.entity.game.GameFinalScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameFinalScoreRepository extends JpaRepository<GameFinalScore, Long> {

    Optional<GameFinalScore> findByGameId(Long gameId);
}