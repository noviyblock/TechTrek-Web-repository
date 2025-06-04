package com.startupgame.entity.game;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.startupgame.modules.game.entity.Game;
import com.startupgame.modules.game.entity.Mission;
import com.startupgame.modules.game.entity.Modifier;
import com.startupgame.modules.game.entity.Team;
import org.junit.jupiter.api.Test;

import com.startupgame.modules.user.User;

class GameTest {

    @Test
    void testNoArgsConstructor() {
        Game game = new Game();
        assertNotNull(game);
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User();
        Mission mission = new Mission();
        Set<Modifier> modifiers = new HashSet<>();
        UUID uuid = UUID.randomUUID();

        Game game = new Game(
                1L,
                "My Startup",
                1.0,
                mission,
                user,
                LocalDateTime.now(),
                null,
                new Team(),
                modifiers,
                uuid,
                0
        );

        assertEquals("My Startup", game.getCompanyName());
        assertEquals(1.0, game.getDifficulty(), 0.001);
        assertEquals(mission, game.getMission());
        assertEquals(user, game.getUser());
        assertNotNull(game.getStartTime());
        assertNull(game.getEndTime());
        assertNotNull(game.getTeam());
        assertEquals(modifiers, game.getModifiers());
        assertEquals(uuid, game.getMlGameId());
        assertEquals(0, game.getScore());
    }

    @Test
    void testSettersAndGetters() {
        Game game = new Game();
        game.setId(1L);
        game.setCompanyName("New Company");
        game.setDifficulty(1.5);

        assertEquals(1L, game.getId());
        assertEquals("New Company", game.getCompanyName());
        assertEquals(1.5, game.getDifficulty(), 0.001);
    }

    @Test
    void testBuilder() {
        UUID uuid = UUID.randomUUID();
        Team team = new Team();
        Set<Modifier> modifiers = new HashSet<>();

        Game game = Game.builder()
                .id(1L)
                .companyName("Future Corp")
                .difficulty(1.2)
                .team(team)
                .modifiers(modifiers)
                .mlGameId(uuid)
                .score(95)
                .build();

        assertEquals(1L, game.getId());
        assertEquals("Future Corp", game.getCompanyName());
        assertEquals(1.2, game.getDifficulty(), 0.001);
        assertEquals(team, game.getTeam());
        assertEquals(modifiers, game.getModifiers());
        assertEquals(uuid, game.getMlGameId());
        assertEquals(95, game.getScore());
    }
}
