package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RollWithMultiplier {
    private RollResponse rollResponse;
    private double multiplier;
}
