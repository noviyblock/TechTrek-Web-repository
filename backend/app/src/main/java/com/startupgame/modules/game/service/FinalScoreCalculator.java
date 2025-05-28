package com.startupgame.modules.game.service;

import com.startupgame.modules.game.dto.game.response.FinalScoreDTO;

final class FinalScoreCalculator {

    private FinalScoreCalculator() {
    }

    static FinalScoreDTO calculate(long money, int tech, int product, int motivation, int monthsPassed, boolean fundedAllStages, boolean motivationNeverBelow50) {

        int moneyScore = money >= 50_000 ? 20 : money >= 20_000 ? 10 : 5;

        int techScore = tech >= 80 ? 25 : tech >= 60 ? 15 : 10;

        int productScore = product >= 80 ? 25 : product >= 60 ? 15 : 10;

        int motivationScore = motivation >= 80 ? 15 : motivation >= 60 ? 10 : 5;

        int timeScore = monthsPassed <= 30 ? 15 : monthsPassed <= 36 ? 10 : 5;

        int bonus = 0;
        if (monthsPassed <= 24) bonus += 5;
        if (fundedAllStages) bonus += 5;
        if (motivationNeverBelow50) bonus += 5;

        if (monthsPassed > 42) bonus -= 5;

        int total = moneyScore + techScore + productScore + motivationScore + timeScore + bonus;

        return FinalScoreDTO.builder()
                .moneyScore(moneyScore)
                .techScore(techScore)
                .productScore(productScore)
                .motivationScore(motivationScore)
                .timeScore(timeScore)
                .bonusScore(bonus)
                .totalScore(total).build();
    }
}

