package com.startupgame.entity.game;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResourcesTest {

    @Test
    void testDefaultConstructorAndGetters() {
        Resources resources = new Resources();
        resources.setMoney(100000);
        resources.setMotivation(75);
        resources.setProductReadiness(60);
        resources.setTechnicReadiness(80);

        assertEquals(100000, resources.getMoney());
        assertEquals(75, resources.getMotivation());
        assertEquals(60, resources.getProductReadiness());
        assertEquals(80, resources.getTechnicReadiness());
    }

    @Test
void testAllArgsConstructor_withBuilder() {
    Resources resources = Resources.builder()
            .money(100000)
            .motivation(75)
            .productReadiness(60)
            .technicReadiness(80)
            .numberOfOffices(2)
            .build();

    assertEquals(100000, resources.getMoney());
    assertEquals(75, resources.getMotivation());
    assertEquals(60, resources.getProductReadiness());
    assertEquals(80, resources.getTechnicReadiness());
    assertEquals(2, resources.getNumberOfOffices());
}
}
