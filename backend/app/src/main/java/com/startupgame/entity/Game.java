package com.startupgame.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double difficulty;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;
}
