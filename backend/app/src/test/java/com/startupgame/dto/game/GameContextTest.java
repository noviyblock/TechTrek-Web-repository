package com.startupgame.dto.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import com.startupgame.modules.game.dto.game.response.GameContext;
import org.junit.jupiter.api.Test;

import com.startupgame.modules.game.entity.Game;
import com.startupgame.modules.game.entity.Resources;
import com.startupgame.modules.game.entity.SuperEmployee;
import com.startupgame.modules.game.entity.Team;
import com.startupgame.modules.game.entity.Turn;

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