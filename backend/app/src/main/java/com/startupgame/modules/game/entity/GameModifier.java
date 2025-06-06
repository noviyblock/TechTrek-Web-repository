package com.startupgame.modules.game.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_modifiers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameModifier {
    public GameModifier(Game game, Modifier modifier) {
        this.game = game;
        this.modifier = modifier;
        this.usageCount = 0;
        this.active = true;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modifier_id", nullable = false)
    private Modifier modifier;

    private Integer usageCount = 0;

    private Boolean active = true;

    private Integer quantity;
}
