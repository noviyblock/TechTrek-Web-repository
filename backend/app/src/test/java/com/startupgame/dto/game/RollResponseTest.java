package com.startupgame.dto.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RollResponseTest {

    @Test
    void testGetters() {
        RollResponse response = new RollResponse(9, "success");

        assertEquals(9, response.getDiceTotal());
        assertEquals("success", response.getZone());
    }
}
