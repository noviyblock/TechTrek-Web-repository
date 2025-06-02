package com.startupgame.entity.game;

import static org.junit.jupiter.api.Assertions.*;

import com.startupgame.modules.game.entity.ActionType;
import com.startupgame.modules.game.entity.Game;
import com.startupgame.modules.game.entity.Resources;
import com.startupgame.modules.game.entity.Turn;
import org.junit.jupiter.api.Test;

class TurnTest {

    @Test
    void testNoArgsConstructor() {
        Turn turn = new Turn();
        assertNotNull(turn);
    }

    @Test
    void testAllArgsConstructor() {
        Game game = new Game();
        Resources resources = new Resources();
        ActionType actionType = new ActionType();

        Turn turn = new Turn(
                1L,
                game,
                resources,
                "Critical bug found",
                "Fix it now",
                8,
                actionType,
                1,
                3,
                90.0
        );

        assertEquals(1L, turn.getId());
        assertEquals(game, turn.getGame());
        assertEquals(resources, turn.getResources());
        assertEquals("Critical bug found", turn.getSituation());
        assertEquals("Fix it now", turn.getAnswer());
        assertEquals(8, turn.getDiceNumber());
        assertEquals(actionType, turn.getActionType());
        assertEquals(1, turn.getStage());
        assertEquals(3, turn.getTurnNumber());
        assertEquals(90.0, turn.getScore(), 0.001);
    }

    @Test
    void testSettersAndGetters() {
        Turn turn = new Turn();
        turn.setId(1L);
        turn.setDiceNumber(7);
        turn.setStage(2);
        turn.setTurnNumber(5);
        turn.setScore(85.5);

        assertEquals(1L, turn.getId());
        assertEquals(7, turn.getDiceNumber());
        assertEquals(2, turn.getStage());
        assertEquals(5, turn.getTurnNumber());
        assertEquals(85.5, turn.getScore(), 0.001);
    }

    @Test
    void testBuilder() {
        Game game = new Game();
        Resources resources = new Resources();
        ActionType actionType = new ActionType();

        Turn turn = Turn.builder()
                .id(1L)
                .game(game)
                .resources(resources)
                .situation("Server down")
                .answer("Reboot")
                .diceNumber(6)
                .actionType(actionType)
                .stage(1)
                .turnNumber(4)
                .score(75.0)
                .build();

        assertEquals(1L, turn.getId());
        assertEquals(game, turn.getGame());
        assertEquals(resources, turn.getResources());
        assertEquals("Server down", turn.getSituation());
        assertEquals("Reboot", turn.getAnswer());
        assertEquals(6, turn.getDiceNumber());
        assertEquals(actionType, turn.getActionType());
        assertEquals(1, turn.getStage());
        assertEquals(4, turn.getTurnNumber());
        assertEquals(75.0, turn.getScore(), 0.001);
    }
}
