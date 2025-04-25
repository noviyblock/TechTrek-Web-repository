package com.startupgame.repository.game;

import com.startupgame.entity.game.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {
}
