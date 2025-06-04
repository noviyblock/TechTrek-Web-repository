package com.startupgame.modules.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sphere")
@Data
public class Sphere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //TODO override method toString
}
