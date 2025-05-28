package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModifierRepository extends JpaRepository<Modifier, Long> {
    Optional<Modifier> findByName(String name);
}
