package com.startupgame.modules.game.dto.ml.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateDecisionRequest {
    private UUID game_id;
    private long money;
    @JsonProperty("technic_readiness")
    private int technicReadiness;
    @JsonProperty("product_readiness")
    private int productReadiness;
    private int motivation;
    @JsonProperty("months_passed")
    private int monthsPassed;
    private int juniors;
    private int middles;
    private int seniors;
    private List<String> c_levels;
    private String decision;
}
