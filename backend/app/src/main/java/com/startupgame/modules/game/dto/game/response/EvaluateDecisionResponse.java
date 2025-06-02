package com.startupgame.modules.game.dto.game.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluateDecisionResponse {
    private final int rawMotivationDelta;
    private final int rawTechnicalReadinessDelta;
    private final int rawProductReadinessDelta;
    private final long rawMoneyDelta;
    private final int scaledMotivationDelta;
    private final int scaledTechnicalReadinessDelta;
    private final int scaledProductReadinessDelta;
    private final long scaledMoneyDelta;
    private final int newMotivation;
    private final int newTechnicalReadiness;
    private final int newProductReadiness;
    private final long newMoney;
    private final String textToPlayer;
    private final double qualityScore;
    private final RollResponse roll;
}
