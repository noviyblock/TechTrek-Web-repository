package com.startupgame.modules.game.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.startupgame.modules.game.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    /**
     * Получает миссии по ID сферы с пагинацией.
     *
     * @param sphereId Идентификатор сферы.
     * @param pageable Параметры пагинации.
     * @return Страница миссий.
     */
    Page<Mission> findBySphereId(Long sphereId, Pageable pageable);
}
