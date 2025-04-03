package com.startupgame.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private Double difficulty;

    @ManyToOne
    private Theme theme;

    //TODO override method toString
}
