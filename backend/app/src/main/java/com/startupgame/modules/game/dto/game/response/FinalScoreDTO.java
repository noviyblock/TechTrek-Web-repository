package com.startupgame.modules.game.dto.game.response;

import lombok.Builder;

@Builder
public record FinalScoreDTO(
        int moneyScore,
        int techScore,
        int productScore,
        int motivationScore,
        int timeScore,
        int bonusScore,
        int totalScore
) {
}
