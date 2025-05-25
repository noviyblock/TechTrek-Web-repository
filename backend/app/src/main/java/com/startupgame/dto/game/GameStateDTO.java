package com.startupgame.dto.game;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
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

    private Long juniors;
    private Long middles;
    private Long seniors;

    private List<String> superEmployees;
    private String officeName;

    private LocalDateTime endTime;

    private String situationText;
    private FinalScoreDTO finalScore;
}
