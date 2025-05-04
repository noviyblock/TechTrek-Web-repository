package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameShortInfoDTO {
    private String sphere;
    private Integer finalScore;
}
