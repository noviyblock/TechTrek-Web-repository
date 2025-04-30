package com.startupgame.entity.game;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "modifiers")
@Data
public class Modifier {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ModifierType type;

    private Long purchaseCost;
    private Long upkeepCost;
    private Integer stageAllowed;
}
