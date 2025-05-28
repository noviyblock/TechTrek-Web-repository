package com.startupgame.modules.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.startupgame.modules.game.entity.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByUserId(Long userId);
}
