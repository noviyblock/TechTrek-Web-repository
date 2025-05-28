package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.SuperEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperEmployeeRepository extends JpaRepository<SuperEmployee, Long> {
    List<SuperEmployee> findByTeamId(long teamId);
}
