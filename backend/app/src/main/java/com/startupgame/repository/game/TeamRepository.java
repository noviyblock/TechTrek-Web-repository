package com.startupgame.repository.game;

import com.startupgame.entity.game.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
