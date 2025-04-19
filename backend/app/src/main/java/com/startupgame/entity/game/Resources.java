package com.startupgame.entity.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resources")
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long money;

    private Integer motivation;

    private Integer productReadiness;

    private Integer technicReadiness;

    private Integer numberOfOffices;
}
