package com.startupgame.entity.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TeamTest {

    @Test
    void testNoArgsConstructor() {
        Team team = new Team();
        assertNotNull(team);
    }

    @Test
    void testAllArgsConstructor() {
        Team team = new Team();
        team.setJuniorAmount(2);
        team.setMiddleAmount(3);
        team.setSeniorAmount(1);

        assertEquals(2, team.getJuniorAmount());
        assertEquals(3, team.getMiddleAmount());
        assertEquals(1, team.getSeniorAmount());
    }

    @Test
    void testSettersAndGetters() {
        Team team = new Team();
        team.setJuniorAmount(5);
        team.setMiddleAmount(4);
        team.setSeniorAmount(1);

        assertEquals(5, team.getJuniorAmount());
        assertEquals(4, team.getMiddleAmount());
        assertEquals(1, team.getSeniorAmount());
    }

    @Test
    void testBuilder() {
        Team team = Team.builder()
                .juniorAmount(2)
                .middleAmount(2)
                .seniorAmount(1)
                .build();

        assertEquals(2, team.getJuniorAmount());
        assertEquals(2, team.getMiddleAmount());
        assertEquals(1, team.getSeniorAmount());
    }
}