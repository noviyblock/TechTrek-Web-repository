package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceDelta {
    private int motivation;
    private int technicalReadiness;
    private int productReadiness;
    private double money;
}
