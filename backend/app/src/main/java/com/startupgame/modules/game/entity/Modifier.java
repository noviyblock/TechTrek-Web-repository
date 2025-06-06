package com.startupgame.modules.game.entity;

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

    @Column(name = "modifier_type")
    @Enumerated(EnumType.STRING)
    private ModifierType modifierType;

    private Long purchaseCost;
    private Long upkeepCost;
    private Integer stageAllowed;
}
