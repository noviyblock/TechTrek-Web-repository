package com.startupgame.dto.ml;

import lombok.Data;

import java.util.Map;

@Data
public class EvaluateDecisionResult {
    private Map<String, Integer> resource_delta;
    private Map<String, Double> applied_mods;
    private String text_to_player;
    private double quality_score;
}