package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
