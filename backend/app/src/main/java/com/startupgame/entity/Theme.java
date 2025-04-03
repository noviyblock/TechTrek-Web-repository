package com.startupgame.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theme")
@Data
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //TODO override method toString
}
