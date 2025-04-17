package com.startupgame.dto.game;

import lombok.Data;

@Data
public class StartGameRequest {
    private Long missionId;
    private String companyName;
}
