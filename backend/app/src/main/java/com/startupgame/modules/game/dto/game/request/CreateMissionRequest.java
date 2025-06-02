package com.startupgame.modules.game.dto.game.request;

import lombok.Data;

@Data
public class CreateMissionRequest {
    private String title;
    private Long sphereId;
}
