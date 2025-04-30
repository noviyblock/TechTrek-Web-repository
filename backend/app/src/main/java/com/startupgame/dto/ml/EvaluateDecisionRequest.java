package com.startupgame.dto.ml;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EvaluateDecisionRequest {
    private String game_id;
    private long money;
    private int technicReadiness;
    private int productReadiness;
    private int motivation;
    private int juniors;
    private int middles;
    private int seniors;
    private List<String> c_levels;
    private String decision;
}
