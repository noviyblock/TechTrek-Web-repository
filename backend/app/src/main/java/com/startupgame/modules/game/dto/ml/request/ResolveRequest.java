package com.startupgame.modules.game.dto.ml.request;

import com.startupgame.modules.game.dto.game.response.GameStateDTO;
import com.startupgame.modules.game.dto.ml.response.EvaluateDecisionResult;
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
