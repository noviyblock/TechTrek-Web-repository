package com.startupgame.entity.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModifierTest {

    @Test
    void testGettersAndSetters() {
        Modifier modifier = new Modifier();
        modifier.setId(1L);
        modifier.setName("CTO Bonus");
        modifier.setModifierType(ModifierType.SENIOR); // ← Исправлено
        modifier.setPurchaseCost(50000L);
        modifier.setUpkeepCost(10000L);
        modifier.setStageAllowed(1);

        assertEquals(1L, modifier.getId());
        assertEquals("CTO Bonus", modifier.getName());
        assertEquals(ModifierType.SENIOR, modifier.getModifierType()); // ← Исправлено
        assertEquals(50000L, modifier.getPurchaseCost());
        assertEquals(10000L, modifier.getUpkeepCost());
        assertEquals(1, modifier.getStageAllowed());
    }
}