package com.startupgame.repository.game;

import com.startupgame.entity.game.SuperEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperEmployeeRepository extends JpaRepository<SuperEmployee, Long> {
    List<SuperEmployee> findByTeamId(long teamId);
}
