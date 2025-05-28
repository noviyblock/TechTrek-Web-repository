package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameShortInfoDTO {
    private Long id;
    private String sphere;
    private Integer finalScore;
}
