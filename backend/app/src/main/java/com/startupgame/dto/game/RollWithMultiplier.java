package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RollWithMultiplier {
    private RollResponse rollResponse;
    private double multiplier;
}
