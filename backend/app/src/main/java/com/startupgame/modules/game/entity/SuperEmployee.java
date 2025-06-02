package com.startupgame.modules.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "super_employee")
@Data
public class SuperEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "bonus")
    private Double bonus;
}
