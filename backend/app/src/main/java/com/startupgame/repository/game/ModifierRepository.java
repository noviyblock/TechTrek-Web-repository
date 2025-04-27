package com.startupgame.repository.game;

import com.startupgame.entity.game.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {
    Optional<Modifier> findByName(String name);
}
