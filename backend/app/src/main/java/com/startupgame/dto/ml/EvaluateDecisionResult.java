package com.startupgame.dto.ml;

import lombok.Data;


@Data
public class EvaluateDecisionResult {
    private String text_to_player;
    private double quality_score;
}