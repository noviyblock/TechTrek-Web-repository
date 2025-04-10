package com.startupgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.startupgame.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
