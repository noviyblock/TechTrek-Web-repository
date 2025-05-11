package com.startupgame.dto.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.startupgame.entity.game.Game;
import com.startupgame.entity.game.Resources;
import com.startupgame.entity.game.SuperEmployee;
import com.startupgame.entity.game.Team;
import com.startupgame.entity.game.Turn;

class GameContextTest {

    @Test
    void testGetters() {
        Game game = new Game();
        Turn turn = new Turn();
        Resources resources = new Resources();
        Team team = new Team();
        SuperEmployee employee = new SuperEmployee();
        UUID uuid = UUID.randomUUID();

        GameContext context = new GameContext(game, turn, resources, team, List.of(employee), uuid);

        assertEquals(game, context.getGame());
        assertEquals(turn, context.getTurn());
        assertEquals(resources, context.getResources());
        assertEquals(team, context.getTeam());
        assertEquals(List.of(employee), context.getSuperEmployees());
        assertEquals(uuid, context.getMlUuid());
    }
}