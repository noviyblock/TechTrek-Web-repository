package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMissionResponse {
    private Long id;
    private String name;
}
