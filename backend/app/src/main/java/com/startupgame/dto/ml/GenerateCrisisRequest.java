package com.startupgame.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GenerateCrisisRequest {
    private String game_id;
    private long money;

    @JsonProperty("technic_readiness")
    private int technicReadiness;
    @JsonProperty("product_readiness")
    private int productReadiness;
    private int motivation;
    @JsonProperty("months_passed")
    private int monthsPassed;
    private Long juniors;
    private Long middles;
    private Long seniors;
    private List<String> c_levels;
}
