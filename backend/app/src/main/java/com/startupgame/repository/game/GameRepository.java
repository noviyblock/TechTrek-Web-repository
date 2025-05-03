package com.startupgame.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import com.startupgame.entity.game.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByUserId(Long userId);
}
