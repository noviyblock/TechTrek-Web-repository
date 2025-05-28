package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.entity.Sphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SphereRepository extends JpaRepository<Sphere, Long> {
}
