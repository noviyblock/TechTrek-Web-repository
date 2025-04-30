package com.startupgame.dto.game;

import com.startupgame.entity.game.ModifierType;
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
