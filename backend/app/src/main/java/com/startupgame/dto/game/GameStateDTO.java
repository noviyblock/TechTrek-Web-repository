package com.startupgame.dto.game;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameStateDTO {
    private Long gameId;
    private String companyName;
    private int stage;
    private int turnNumber;
    private int monthsPassed;
    private long missionId;

    private long money;
    private int technicReadiness;
    private int productReadiness;
    private int motivation;

    private int juniors;
    private int middles;
    private int seniors;

    private List<String> superEmployees;
    private int numberOfOffices;

    private String situationText;
}
