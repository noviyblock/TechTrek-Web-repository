package com.startupgame.modules.game.dto.game.response;

import com.startupgame.modules.game.entity.ModifierType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifierResponse {
    private Long id;

    private String name;

    private ModifierType type;

    private Long purchaseCost;

    private Long upkeepCost;

    private Integer stageAllowed;

    private boolean owned;
}
