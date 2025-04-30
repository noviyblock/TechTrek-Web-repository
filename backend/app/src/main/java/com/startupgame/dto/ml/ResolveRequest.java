package com.startupgame.dto.ml;

import com.startupgame.dto.game.GameStateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResolveRequest {
    private GameStateDTO state;
    private EvaluateDecisionResult pre_roll;
    private Integer dice_total;
    private String zone;
}
