package com.startupgame.entity.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_final_score")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GameFinalScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private Integer moneyScore;
    private Integer techScore;
    private Integer productScore;
    private Integer motivationScore;
    private Integer timeScore;
    private Integer bonusScore;
    private Integer totalScore;
}
