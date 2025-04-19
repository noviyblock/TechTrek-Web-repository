package com.startupgame.entity.game;

import jakarta.persistence.*;

@Entity
@Table(name = "action_type")
public class ActionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
