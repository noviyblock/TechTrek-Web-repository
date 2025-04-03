package com.startupgame.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(UserGameId.class)
@Table(name = "user_game")
public class UserGame {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "start_game_time")
    private LocalDateTime startGameTime;

    @Column(name = "end_game_time")
    private LocalDateTime endGameTime;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private RegisteredUser user;

    //TODO override method toString()
}
