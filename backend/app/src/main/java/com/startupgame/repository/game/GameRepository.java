package com.startupgame.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import com.startupgame.entity.game.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
