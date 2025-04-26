package com.startupgame.repository.game;

import com.startupgame.entity.game.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnRepository extends JpaRepository<Turn, Long> {
}
