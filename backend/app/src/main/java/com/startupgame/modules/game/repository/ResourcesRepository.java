package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

}
