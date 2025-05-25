package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RollResponse {
    private int diceTotal;
    private int firstCubeRoll;
    private int secondCubeRoll;
    private String zone;
}

