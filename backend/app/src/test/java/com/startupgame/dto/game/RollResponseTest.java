package com.startupgame.dto.game;

import static org.junit.jupiter.api.Assertions.*;

import com.startupgame.modules.game.dto.game.response.RollResponse;
import org.junit.jupiter.api.Test;

class RollResponseTest {

    @Test
    void testGetters() {
        RollResponse response = new RollResponse(9, 6, 3, "success");

        assertEquals(9, response.getDiceTotal());
        assertEquals("success", response.getZone());
    }
}
