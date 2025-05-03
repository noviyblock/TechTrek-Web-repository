package com.startupgame.repository.game;

import com.startupgame.dto.game.DeveloperCounts;
import com.startupgame.entity.game.GameModifier;
import com.startupgame.entity.game.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameModifierRepository extends JpaRepository<GameModifier, Long> {
    List<GameModifier> findByGameId(Long gameId);
    Optional<GameModifier> findByGameIdAndModifierId(Long gameId, Long modifierId);
    @Query("""
        SELECT new com.startupgame.dto.game.DeveloperCounts(
            COALESCE(SUM(CASE WHEN m.modifierType = 'JUNIOR'  THEN gm.quantity END),0),
            COALESCE(SUM(CASE WHEN m.modifierType = 'MIDDLE'  THEN gm.quantity END),0),
            COALESCE(SUM(CASE WHEN m.modifierType = 'SENIOR'  THEN gm.quantity END),0)
        )
        FROM GameModifier gm
        JOIN gm.modifier m
        WHERE gm.game.id = :gameId
    """)
    DeveloperCounts findDeveloperCounts(@Param("gameId") Long gameId);

    // офис, купленный в игре (берём активный, последний по id)
    @Query("""
        SELECT m
        FROM GameModifier gm
        JOIN gm.modifier m
        WHERE gm.game.id = :gameId
          AND m.modifierType LIKE 'OFFICE_%'
          AND gm.active = true
        ORDER BY gm.id DESC
    """)
    Optional<Modifier> findActiveOffice(@Param("gameId") Long gameId);
}
