package com.startupgame.dto.game;

import lombok.Data;

@Data
public class CreateMissionRequest {
    private String title;
    private Long sphereId;
}
