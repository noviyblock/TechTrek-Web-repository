package com.startupgame.entity.game;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theme")
@Data
public class Sphere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //TODO override method toString
}
