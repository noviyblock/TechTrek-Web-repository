package com.startupgame.modules.game.dto.ml.response;

import lombok.Data;


@Data
public class EvaluateDecisionResult {
    private String text_to_player;
    private double quality_score;
}