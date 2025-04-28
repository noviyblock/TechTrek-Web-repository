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
@Table(name = "turn")
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "resources_id", nullable = false)
    private Resources resources;

    @Column
    private String situation;

    @Column
    private String answer;

    private Integer diceNumber;

    @ManyToOne
    @JoinColumn(name = "action_type_id")
    private ActionType actionType;

    private Integer stage;

    private Integer turnNumber;

    private Double score;
}
