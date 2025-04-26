package com.startupgame.repository.game;

import com.startupgame.entity.game.Sphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SphereRepository extends JpaRepository<Sphere, Long> {
}
