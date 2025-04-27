package com.startupgame.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluateDecisionResponse {
    private int motivationDelta;
    private int newMotivation;
    private int technicalDelta;
    private int newTechnicalReadiness;
    private int productDelta;
    private int newProductReadiness;
    private double moneyDelta;
    private double newMoney;
    private String textToPlayer;
    private double qualityScore;
}
