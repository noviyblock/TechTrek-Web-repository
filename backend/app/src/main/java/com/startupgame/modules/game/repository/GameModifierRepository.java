package com.startupgame.modules.game.repository;

import com.startupgame.modules.game.dto.game.response.DeveloperCounts;
import com.startupgame.modules.game.entity.GameModifier;
import com.startupgame.modules.game.entity.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameModifierRepository extends JpaRepository<GameModifier, Long> {
    List<GameModifier> findByGameId(Long gameId);

    Optional<GameModifier> findByGameIdAndModifierId(Long gameId, Long modifierId);

    @Query("""
                SELECT new com.startupgame.modules.game.dto.game.response.DeveloperCounts (
                    COALESCE(SUM(CASE WHEN m.modifierType = com.startupgame.modules.game.entity.ModifierType.JUNIOR THEN gm.quantity END), 0),
                    COALESCE(SUM(CASE WHEN m.modifierType = com.startupgame.modules.game.entity.ModifierType.MIDDLE THEN gm.quantity END), 0),
                    COALESCE(SUM(CASE WHEN m.modifierType = com.startupgame.modules.game.entity.ModifierType.SENIOR THEN gm.quantity END), 0)
                )
                FROM GameModifier gm
                JOIN gm.modifier m
                WHERE gm.game.id = :gameId
            """)
    DeveloperCounts findDeveloperCounts(@Param("gameId") Long gameId);

    @Query("""
                SELECT m.name
                FROM GameModifier gm
                JOIN gm.modifier m
                WHERE gm.game.id = :gameId
                  AND m.modifierType IN (
                       com.startupgame.modules.game.entity.ModifierType.C_LEVEL
                  )
                  AND gm.active = true
            """)
    List<String> findCLevelNames(@Param("gameId") Long gameId);

    @Query("""
                SELECT m
                FROM GameModifier gm
                JOIN gm.modifier m
                WHERE gm.game.id = :gameId
                  AND m.modifierType IN (
                       com.startupgame.modules.game.entity.ModifierType.OFFICE
                  )
                  AND gm.active = true
                ORDER BY gm.id DESC
            """)
    Optional<Modifier> findActiveOffice(@Param("gameId") Long gameId);

    @Query("""
                SELECT COALESCE(SUM(gm.quantity * m.upkeepCost), 0)
                FROM GameModifier gm
                JOIN gm.modifier m
                WHERE gm.game.id = :gameId
                  AND gm.active = true
            """)
    Long sumUpkeepCosts(@Param("gameId") Long gameId);

}
