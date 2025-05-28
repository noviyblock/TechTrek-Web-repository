package com.startupgame.modules.game.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "action_type")
public class ActionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
