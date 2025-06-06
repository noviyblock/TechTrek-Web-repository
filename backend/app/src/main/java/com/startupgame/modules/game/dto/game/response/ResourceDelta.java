package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceDelta {
    private int motivation;
    private int technicalReadiness;
    private int productReadiness;
    private long money;
}
